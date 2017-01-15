package de.canitzp.ctpcore.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public class NBTUtil{

    public static NBTTagCompound setItemStack(NBTTagCompound compound, String key, ItemStack value){
        NBTTagCompound nbt = new NBTTagCompound();
        value.writeToNBT(nbt);
        compound.setTag(key, nbt);
        return compound;
    }

    public static ItemStack getItemStack(NBTTagCompound compound, String key){
        NBTTagCompound nbt = (NBTTagCompound) compound.getTag(key);
        return nbt != null ? new ItemStack(nbt) : ItemStack.EMPTY;
    }

}
