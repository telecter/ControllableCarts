package xyz.telecter.controllablecarts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record VehicleFuelPayload(int id, float fuel) implements CustomPacketPayload {
    public static final Type<VehicleFuelPayload> ID = CustomPacketPayload.createType("vehicle_fuel");
    public static final StreamCodec<FriendlyByteBuf, VehicleFuelPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, VehicleFuelPayload::id, ByteBufCodecs.FLOAT, VehicleFuelPayload::fuel, VehicleFuelPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}