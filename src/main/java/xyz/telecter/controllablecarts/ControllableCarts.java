package xyz.telecter.controllablecarts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.telecter.controllablecarts.entity.ControllableMinecart;
import xyz.telecter.controllablecarts.entity.ModEntityType;

public class ControllableCarts implements ModInitializer {
    public static final String MOD_ID = "controllablecarts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean firstTickRan = false;

	@Override
	public void onInitialize() {
        ModEntityType.initialize();
        ModItems.initialize();

        PayloadTypeRegistry.playC2S().register(VehicleMovePayload.ID, VehicleMovePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(VehicleFuelPayload.ID, VehicleFuelPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(VehicleMovePayload.ID, ((payload, context) -> {
            Entity vehicle = context.player().getVehicle();
            if (vehicle instanceof ControllableMinecart cart) {
                cart.move(payload.direction());
            }
        }));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!firstTickRan) {
                firstTickRan = true;
                ControllableMinecart.checkAllEntities(server);
            }

        });
	}
}