package xyz.telecter.controllablecarts.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import xyz.telecter.controllablecarts.ControllableCarts;

public class ModEntityType {
    public static void initialize() {
    }
    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ControllableCarts.MOD_ID, id);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, location, builder.build(ResourceKey.create(Registries.ENTITY_TYPE, location)));
    }

    public static final EntityType<ControllableMinecart> CONTROLLABLE_MINECART = register("controllable_minecart", EntityType.Builder.of(ControllableMinecart::new, MobCategory.MISC).noLootTable().sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
}