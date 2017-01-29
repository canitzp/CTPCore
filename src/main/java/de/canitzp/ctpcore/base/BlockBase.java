package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.WorldGenerator;
import de.canitzp.ctpcore.registry.IRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class BlockBase extends Block implements IRegistryEntry {

    public static Map<Block, String> oreDicts = new HashMap<>();

    private ResourceLocation resource;

    public BlockBase(Material material, ResourceLocation resource) {
        super(material);
        this.resource = resource;
        this.setUnlocalizedName(resource.toString());
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        return (BlockBase) super.setCreativeTab(tab);
    }

    @Override
    public IRegistryEntry[] getRegisterElements() {
        return new IRegistryEntry[]{this, new ItemBlockBase(this)};
    }

    @Override
    public ResourceLocation getRegisterName() {
        return this.resource;
    }

    @Override
    public void onRegister(IRegistryEntry[] otherEntries) {
    }

    @Override
    public void ownRegistry() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer() {

    }

    /**
     * @param blockToSpawnInside The Block it'll spawn inside. e.g.: Coal='net.minecraft.init.Blocks.stone'
     * @param minY The minimal height for the Block to spawn. e.g.: Coal=0
     * @param maxY The maximal height for the Block to spawn. e.g.: Coal=128
     * @param veinSize The maximal amount of Blocks at one place. e.g.: Coal=17
     * @param chance The Chance to spawn the Block. e.g.: Coal=20
     * The example values you'll find in the class: {@link net.minecraft.world.gen.ChunkProviderSettings}
     */
    public BlockBase spawnInWorld(Block blockToSpawnInside, int chance, int maxY, int minY, int veinSize, int dimension){
        WorldGenerator.addBlockSpawn(this, blockToSpawnInside, chance, maxY, minY, veinSize, dimension);
        return this;
    }

    public BlockBase addOreDictionary(String entryName){
        oreDicts.put(this, entryName);
        return this;
    }

}
