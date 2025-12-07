package xyz.telecter.controllablecarts.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MinecartDataPayload(int id, float fuel, double maxSpeed) implements CustomPacketPayload {
    public static final Type<MinecartDataPayload> ID = CustomPacketPayload.createType("minecart_data");
    public static final StreamCodec<FriendlyByteBuf, MinecartDataPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MinecartDataPayload::id,
            ByteBufCodecs.FLOAT, MinecartDataPayload::fuel,
            ByteBufCodecs.DOUBLE, MinecartDataPayload::maxSpeed,
            MinecartDataPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}