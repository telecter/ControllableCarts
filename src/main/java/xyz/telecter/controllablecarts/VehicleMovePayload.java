package xyz.telecter.controllablecarts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record VehicleMovePayload(Vec3 direction) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VehicleMovePayload> ID = CustomPacketPayload.createType("vehicle_move");
    public static final StreamCodec<FriendlyByteBuf, VehicleMovePayload> CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, VehicleMovePayload::direction, VehicleMovePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}