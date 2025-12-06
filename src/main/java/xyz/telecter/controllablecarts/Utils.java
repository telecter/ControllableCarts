package xyz.telecter.controllablecarts;

import net.minecraft.world.entity.Entity;

public class Utils {

    public static double getEntitySpeed(Entity entity) {
        double deltaX = entity.getX() - entity.xOld;
        double deltaZ = entity.getZ() - entity.zOld;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
    }
}
