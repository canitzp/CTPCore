package de.canitzp.ctpcore.util;

import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class StackUtil{

    public static boolean canMerge(ItemStack one, ItemStack two){
        return ItemStack.areItemsEqual(one, two) && one.getMetadata() == two.getMetadata() && one.getCount() + two.getCount() <= one.getMaxStackSize();
    }

    /** Furnace method
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2){
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == Short.MAX_VALUE || stack2.getMetadata() == stack1.getMetadata());
    }

}
