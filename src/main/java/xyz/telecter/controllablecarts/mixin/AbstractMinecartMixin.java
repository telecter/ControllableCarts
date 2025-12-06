package xyz.telecter.controllablecarts.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartFurnace.class)
public abstract class AbstractMinecartMixin {

    @Inject(method = "interact", at = @At("RETURN"))
    private void controllablecarts_defineData(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
    }
}
