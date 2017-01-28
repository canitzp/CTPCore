package de.canitzp.ctpcore.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class WorldUtil{

    /**
     * This method to get if it is daytime does work at client too.
     */
    public static boolean isDaytime(World world){
        return world.getWorldTime() % 24000 >= 13000;
    }

    public static boolean canBlockSeeSky(World world, BlockPos pos){
        for(int i = pos.getY(); i <= world.getHeight(); i++){
            if(!BlockUtil.isBlockOpace(world, pos, world.getBlockState(pos))){
                return false;
            }
        }
        return true;
    }

}
