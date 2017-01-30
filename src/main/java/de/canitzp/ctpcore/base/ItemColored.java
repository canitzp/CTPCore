package de.canitzp.ctpcore.base;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemColored extends ItemBase implements IItemColor{

    private int color;

    public ItemColored(ResourceLocation name, int color){
        super(name);
        this.color = color;
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex){
        if(!stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("ItemColor")){
            return stack.getTagCompound().getInteger("ItemColor");
        }
        return this.color;
    }

}
