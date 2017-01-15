package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

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

    @Override
    public void registerRenderer(){

    }
}
