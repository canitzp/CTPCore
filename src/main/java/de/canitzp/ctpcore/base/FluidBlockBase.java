package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.render.RenderingRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class FluidBlockBase extends BlockFluidClassic implements IRegistryEntry {

    private FluidBase fluid;

    public FluidBlockBase(FluidBase fluid, Material material) {
        super(fluid, material);
        this.fluid = fluid;
        this.setUnlocalizedName(fluid.getRegisterName().getResourceDomain() + ":" + fluid.getName());
    }

    @Override
    public IRegistryEntry[] getRegisterElements() {
        return new IRegistryEntry[]{this, new ItemBlockBase(this)};
    }

    @Override
    public ResourceLocation getRegisterName() {
        return this.fluid.getRegisterName();
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
