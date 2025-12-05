package xyz.telecter.controllablecarts.mixin;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {


    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void controllablecarts_defineData(CallbackInfo ci) {

    }
}
