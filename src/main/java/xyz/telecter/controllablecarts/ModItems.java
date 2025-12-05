package xyz.telecter.controllablecarts;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MinecartItem;
import xyz.telecter.controllablecarts.entity.ModEntityType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ModItems {
    public static void initialize() {
        List<ResourceKey<CreativeModeTab>> tabs = Arrays.asList(CreativeModeTabs.REDSTONE_BLOCKS, CreativeModeTabs.TOOLS_AND_UTILITIES);
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            ItemGroupEvents.modifyEntriesEvent(tab).register(itemGroup -> itemGroup.accept(CONTROLLABLE_MINECART));
        }
    }

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ControllableCarts.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static Item CONTROLLABLE_MINECART = register("controllable_minecart", (settings) -> new MinecartItem(ModEntityType.CONTROLLABLE_MINECART, settings), new Item.Properties().stacksTo(1));
}
