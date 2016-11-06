package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.render.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemBlockBase extends ItemBlock implements IRegistryEntry {

    public ItemBlockBase(Block block) {
        super(block);
    }

    @Override
    public IRegistryEntry[] getRegisterElements() {
        return new IRegistryEntry[]{this};
    }

    @Override
    public ResourceLocation getRegisterName() {
        return this.block.getRegistryName();
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
        RenderingRegistry.addRenderer(this);
    }
}
