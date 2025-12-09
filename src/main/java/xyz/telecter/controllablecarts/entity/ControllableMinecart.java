package xyz.telecter.controllablecarts.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.telecter.controllablecarts.ModItems;
import xyz.telecter.controllablecarts.Utils;
import xyz.telecter.controllablecarts.packet.MinecartDataPayload;

public class ControllableMinecart extends Minecart {

    private static final float ACCEL = 0.025f;
    private static final float DECEL_FACTOR = 0.90f;

    private static final float FUEL_SPEND = 0.005f;
    private static final float MAX_FUEL = 100;

    private float fuel;

    @Environment(EnvType.CLIENT)
    public double maxSpeed;

    public ControllableMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private void accelerate(Vec3 direction) {
        setDeltaMovement(getDeltaMovement().add(direction.scale(ACCEL)));
    }

    private void decelerate() {
        setDeltaMovement(getDeltaMovement().scale(DECEL_FACTOR));
    }

    public void move(Vec3 direction) {
        if (level() instanceof ServerLevel level) {
            if (direction.equals(Vec3.ZERO)) {
                decelerate();
            } else {
                double speed = Utils.getEntitySpeed(this);
                if (fuel > 0 && speed <= getMaxSpeed(level)) {
                    if (speed > 0.05) {
                        fuel -= FUEL_SPEND;
                    }
                    if (getFirstPassenger() instanceof ServerPlayer player) {
                        sendFuelUpdate(player);
                    }
                    accelerate(direction);
                }
            }
        }

    }


    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        ItemStack item = player.getMainHandItem();
        if (item.is(ItemTags.FURNACE_MINECART_FUEL) || item.is(Items.COAL_BLOCK)) {
            int amount = item.is(Items.COAL_BLOCK) ? 9 : 1;
            if (Math.floor(fuel) + amount > MAX_FUEL) {
                player.displayClientMessage(Component.translatable("entity.controllablecarts.controllable_minecart.full").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }
            fuel += amount;
            item.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, interactionHand);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.fuel = valueInput.getFloatOr("Fuel", 0.0f);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putFloat("Fuel", fuel);
    }


    @Override
    protected void addPassenger(Entity entity) {
        super.addPassenger(entity);
        if (entity instanceof ServerPlayer player) {
            sendFuelUpdate(player);
        }
    }


    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public void sendFuelUpdate(ServerPlayer player) {
        if (level() instanceof ServerLevel level) {
            ServerPlayNetworking.send(player, new MinecartDataPayload(getId(), fuel, getMaxSpeed(level)));
        }
    }

    @Override
    protected @NotNull Item getDropItem() {
        return ModItems.CONTROLLABLE_MINECART;
    }

    @Override
    public @NotNull ItemStack getPickResult() {
        return new ItemStack(ModItems.CONTROLLABLE_MINECART);
    }

    @Override
    public void kill(ServerLevel level) {
        spawnAtLocation(level, new ItemStack(Items.COAL, (int) Math.floor(fuel)));
        super.kill(level);
    }

    public static void checkAllEntities(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof ControllableMinecart cart && entity.getFirstPassenger() instanceof ServerPlayer player) {
                    cart.sendFuelUpdate(player);
                }
            }
        }
    }
}
