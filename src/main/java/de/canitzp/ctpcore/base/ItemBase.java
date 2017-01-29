package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.render.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemBase extends Item implements IRegistryEntry{

    private ResourceLocation name;

    public ItemBase(ResourceLocation name){
        this.name = name;
        this.setUnlocalizedName(name.toString());
    }

    @Override
    public IRegistryEntry[] getRegisterElements(){
        return new IRegistryEntry[]{this};
    }

    @Override
    public ResourceLocation getRegisterName(){
        return this.name;
    }

    @Override
    public void onRegister(IRegistryEntry[] otherEntries){

    }

    @Override
    public void ownRegistry(){

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(){
        RenderingRegistry.addRenderer(this);
    }
}
