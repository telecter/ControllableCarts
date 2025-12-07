package xyz.telecter.controllablecarts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import xyz.telecter.controllablecarts.entity.ControllableMinecart;
import xyz.telecter.controllablecarts.entity.ModEntityType;
import xyz.telecter.controllablecarts.packet.MinecartDataPayload;
import xyz.telecter.controllablecarts.packet.MinecartMovePayload;


public class ControllableCartsClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntityType.CONTROLLABLE_MINECART, ctx -> new MinecartRenderer(ctx, ModelLayers.MINECART));
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndClientTick);
        CartHud.register();

        ClientPlayNetworking.registerGlobalReceiver(MinecartDataPayload.ID, ((payload, context) -> {
            Entity entity = context.player().level().getEntity(payload.id());
            if (entity instanceof ControllableMinecart cart) {
                cart.setFuel(payload.fuel());
                cart.maxSpeed = payload.maxSpeed();
            } else {
                ControllableCarts.LOGGER.info("Got bad entity from fuel update packet");
            }
        }));

    }

    public void onEndClientTick(Minecraft client) {
        if (client.player != null) {
            Entity vehicle = client.player.getVehicle();
            if (vehicle instanceof ControllableMinecart) {
                Vec3 velocity = vehicle.getDeltaMovement();
                Vec3 direction = client.player.getDirection().getUnitVec3().horizontal();
                if (client.player.input.keyPresses.forward()) {
                    ClientPlayNetworking.send(new MinecartMovePayload(direction));
                } else if (client.player.input.keyPresses.backward() && velocity.length() > 0) {
                    ClientPlayNetworking.send(new MinecartMovePayload(Vec3.ZERO));
                }
            }
        }
    }

}