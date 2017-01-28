package de.canitzp.ctpcore.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandler;

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

    public static NBTTagCompound writeInventory(IItemHandler handler){
        NBTTagCompound nbt = new NBTTagCompound();
        if(handler.getSlots() > 0){
            NBTTagList tagList = new NBTTagList();
            for(int currentIndex = 0; currentIndex < handler.getSlots(); currentIndex++){
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte)currentIndex);
                ItemStack stack = handler.getStackInSlot(currentIndex);
                if(!stack.isEmpty()){
                    stack.writeToNBT(tagCompound);
                }
                tagList.appendTag(tagCompound);
            }
            nbt.setTag("Items", tagList);
        }
        return nbt;
    }

    public static void readInventory(NBTTagCompound nbt, IItemHandler handler){
        if(handler.getSlots() > 0){
            NBTTagList tagList = nbt.getTagList("Items", 10);
            for(int i = 0; i < tagList.tagCount(); i++){
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                byte slotIndex = tagCompound.getByte("Slot");
                if(slotIndex >= 0 && slotIndex < handler.getSlots()){
                    handler.insertItem(slotIndex, new ItemStack(tagCompound), false);
                }
            }
        }
    }

}
