package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.render.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class BlockBase extends Block implements IRegistryEntry {

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
}
