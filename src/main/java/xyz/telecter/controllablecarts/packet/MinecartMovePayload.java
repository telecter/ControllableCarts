package xyz.telecter.controllablecarts.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record MinecartMovePayload(Vec3 direction) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MinecartMovePayload> ID = CustomPacketPayload.createType("minecart_move");
    public static final StreamCodec<FriendlyByteBuf, MinecartMovePayload> CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, MinecartMovePayload::direction, MinecartMovePayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}