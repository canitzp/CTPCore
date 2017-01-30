package de.canitzp.ctpcore.recipe;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public interface ICustomRecipe extends IRegistryEntry{

    @Override
    default IRegistryEntry[] getRegisterElements(){
        return new IRegistryEntry[]{this};
    }

    @Override
    default ResourceLocation getRegisterName(){
        return null;
    }

    @Override
    default void onRegister(IRegistryEntry[] otherEntries){}

    @Override
    default void ownRegistry(){
        RecipeRegistry.addRecipe(this);
    }

    @Override
    default void registerRenderer(){}

    ItemStack getKey();

    String getCategory();

}
