package de.canitzp.ctpcore.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class BlockUtil{

    public static boolean isBlockOpace(World world, BlockPos pos, IBlockState state){
        return state == Blocks.AIR.getDefaultState() || !state.isBlockNormalCube() || !state.isFullBlock() || !state.isFullCube() || !state.isNormalCube() || state.isOpaqueCube();
    }

}
