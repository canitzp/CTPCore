package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.registry.MCRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class FluidBase extends Fluid implements IRegistryEntry {

    private Material fluidBlockMaterial = Material.WATER;

    public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
    }

    public FluidBase setFluidBlockMaterial(Material material){
        this.fluidBlockMaterial = material;
        return this;
    }

    @Override
    public IRegistryEntry[] getRegisterElements() {
        return new IRegistryEntry[]{this};
    }

    @Override
    public ResourceLocation getRegisterName() {
        return new ResourceLocation(this.getStill().getResourceDomain(), this.getName());
    }

    @Override
    public void onRegister(IRegistryEntry[] otherEntries) {

    }

    @Override
    public void ownRegistry() {
        FluidRegistry.registerFluid(this);
        FluidRegistry.addBucketForFluid(this);
        //Has to happen here, cause the FluidBlock needs a registered Fluid
        MCRegistry.register(new FluidBlockBase(this, this.fluidBlockMaterial));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer() {
        Item item = Item.getItemFromBlock(this.block);
        if(item != Items.AIR){
            final ModelResourceLocation loc = new ModelResourceLocation(new ResourceLocation(this.getRegisterName().getResourceDomain(), "fluids"), this.getName());
            ItemMeshDefinition mesh = stack -> loc;
            StateMapperBase mapper = new StateMapperBase(){
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state){
                    return loc;
                }
            };
            ModelLoader.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, mesh);
            ModelLoader.setCustomStateMapper(this.block, mapper);
        } else {
            throw new NullPointerException("The Fluid '" + this.getName() + "' has no item block");
        }
    }
}
