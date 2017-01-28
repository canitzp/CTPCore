package de.canitzp.ctpcore;

import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author canitzp
 */
public class WorldGenerator implements IWorldGenerator{

    private static List<Props> spawner = new ArrayList<>();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){
        if(world.getWorldType() != WorldType.FLAT){
            for(Props prop : spawner){
                if(world.provider.getDimension() == prop.dimension){
                    addOreSpawn(prop.block, prop.blockToSpawnInside, world, random, chunkX*16, chunkZ*16, prop.veinSize, prop.chance, prop.minY, prop.maxY);
                }
            }
        }
    }

    public void addOreSpawn(Block block, Block blockIn, World world, Random random, int blockXPos, int blockZPos, int maxVeinSize, int chancesToSpawn, int minY, int maxY){
        if(maxY > minY){
            int yDiff = maxY-minY;
            for(int i = 0; i < chancesToSpawn; i++){
                int posX = blockXPos+random.nextInt(16);
                int posY = minY+random.nextInt(yDiff);
                int posZ = blockZPos+random.nextInt(16);
                new WorldGenMinable(block.getDefaultState(), maxVeinSize, BlockMatcher.forBlock(blockIn)).generate(world, random, new BlockPos(posX, posY, posZ));
            }
        }
    }

    public static void addBlockSpawn(Block block, Block blockToSpawnInside, int chance, int maxY, int minY, int veinSize, int dimension){
        spawner.add(new Props(block, blockToSpawnInside, chance, maxY, minY, veinSize, dimension));
    }

    private static class Props{
        private int minY, maxY, veinSize, chance, dimension;
        private Block block, blockToSpawnInside;
        private Props(Block block, Block blockToSpawnInside, int chance, int maxY, int minY, int veinSize, int dimension) {
            this.chance = chance;
            this.maxY = maxY;
            this.minY = minY;
            this.veinSize = veinSize;
            this.blockToSpawnInside = blockToSpawnInside;
            this.dimension = dimension;
            this.block = block;
        }
    }

}
