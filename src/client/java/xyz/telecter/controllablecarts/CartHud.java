package xyz.telecter.controllablecarts;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.telecter.controllablecarts.entity.ControllableMinecart;

public class CartHud {
    private static final Minecraft client = Minecraft.getInstance();

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.fromNamespaceAndPath(ControllableCarts.MOD_ID, "minecart_hud"), CartHud::render);
    }

    public static void render(GuiGraphics context, DeltaTracker tickCounter) {
        if (client.player == null) {
            return;
        }
        Player player = client.player;

        int width = 180;
        int height = 30;

        int x = context.guiWidth() / 2;
        int y = context.guiHeight() - 70;

        if (client.player.getVehicle() instanceof ControllableMinecart cart) {
            int bps = Math.toIntExact(
                    Math.round(
                            Math.clamp(Utils.getEntitySpeed(player), 0, cart.maxSpeed) * 20
                    )
            );
            Component speedText = Component.literal(String.valueOf(bps)).append(" bl/s");

            int fuel = Math.round(cart.getFuel());
            Component fuelText = Component.literal(String.valueOf(fuel)).withStyle(fuel > 0 ? ChatFormatting.WHITE : ChatFormatting.RED);

            context.drawString(client.font, speedText, x - width / 2, y + height / 2 - client.font.lineHeight / 2, 0xFFFFFFFF, true);
            context.drawString(client.font, fuelText, x + width / 2 - 20, y + height / 2 - client.font.lineHeight / 2, 0xFFFFFFFF, true);
            context.renderFakeItem(new ItemStack(Items.COAL), x + width / 2 - 40, y + height / 2 - 8);
        }
    }
}
