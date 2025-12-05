package xyz.telecter.controllablecarts;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.telecter.controllablecarts.entity.ControllableMinecart;

public class CartHud {
    private static final Minecraft mc = Minecraft.getInstance();


    static int width = 250;
    static int height = 30;


    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, ResourceLocation.fromNamespaceAndPath(ControllableCarts.MOD_ID, "minecart_hud"), CartHud::render);
    }

    public static void render(GuiGraphics context, DeltaTracker tickCounter) {
        if (mc.player == null) {
            return;
        }
        Player player = mc.player;
        int x = context.guiWidth()/2-width/2;
        int y = context.guiHeight()-80;

        if (mc.player.getVehicle() instanceof ControllableMinecart cart) {

            double deltaX = player.getX() - player.xOld;
            double deltaZ = player.getZ() - player.zOld;
            double speed = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2)) * 20;

            int bps = Math.toIntExact(Math.round(speed));
            Component speedText = Component.literal(String.valueOf(bps)).append(" blocks/second").withStyle(ChatFormatting.BOLD);

            int fuel = Math.round(cart.getFuel());

            Component fuelText = Component.literal(String.valueOf(fuel)).withStyle(fuel > 0 ? ChatFormatting.WHITE : ChatFormatting.RED);


            context.fill(x, y, x+width, y+height, 0x40000000);
            context.renderFakeItem(new ItemStack(Items.COAL), x+width-width/3, y+height/2-8);
            context.drawString(mc.font, speedText, x+10, y+height/2-mc.font.lineHeight/2, 0xFFFFFFFF, false);
            context.drawString(mc.font, fuelText, x+width-width/3+20, y+height/2-mc.font.lineHeight/2, 0xFFFFFFFF, false);
        }
    }
}
