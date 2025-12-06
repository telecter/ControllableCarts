package xyz.telecter.controllablecarts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.telecter.controllablecarts.entity.ControllableMinecart;
import xyz.telecter.controllablecarts.entity.ModEntityType;
import xyz.telecter.controllablecarts.packet.MinecartDataPayload;
import xyz.telecter.controllablecarts.packet.MinecartMovePayload;

public class ControllableCarts implements ModInitializer {
    public static final String MOD_ID = "controllablecarts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private boolean firstTickRan = false;
    private int ticksPassed = 0;

    @Override
    public void onInitialize() {
        ModEntityType.initialize();
        ModItems.initialize();

        PayloadTypeRegistry.playC2S().register(MinecartMovePayload.ID, MinecartMovePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MinecartDataPayload.ID, MinecartDataPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MinecartMovePayload.ID, ((payload, context) -> {
            Entity vehicle = context.player().getVehicle();
            if (vehicle instanceof ControllableMinecart cart) {
                cart.move(payload.direction());
            }
        }));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!firstTickRan) {
                ticksPassed++;
                if (ticksPassed >= 10) {
                    firstTickRan = true;
                    ControllableMinecart.checkAllEntities(server);
                }
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            firstTickRan = false;
            ticksPassed = 0;
        });
        LOGGER.info("{} initialized", MOD_ID);
    }
}