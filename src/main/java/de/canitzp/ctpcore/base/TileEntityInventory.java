package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.util.NBTSaveType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * @author canitzp
 */
public class TileEntityInventory extends TileEntityBase implements ISidedInventory {

    public ItemStack[] slots;
    public String invName;
    public SidedInvWrapper[] invWrappers = new SidedInvWrapper[6];

    public TileEntityInventory(ResourceLocation resource, int slotAmount){
        slots = new ItemStack[slotAmount];
        this.invName = "Inventory" + resource.getResourcePath();
        for(EnumFacing side : EnumFacing.values()){
            this.invWrappers[side.getIndex()] = new SidedInvWrapper(this, side);
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if(this.slots.length > 0){
            int[] theInt = new int[slots.length];
            for(int i = 0; i < theInt.length; i++){
                theInt[i] = i;
            }
            return theInt;
        }
        else{
            return new int[0];
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public boolean isEmpty(){
        for(ItemStack stack : slots){
            if(!stack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.slots[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(!slots[index].isEmpty()){
            ItemStack stackAt;
            if(slots[index].getCount() <= count){
                stackAt = slots[index];
                slots[index] = ItemStack.EMPTY;
                this.markDirty();
                return stackAt;
            }
            else{
                stackAt = slots[index].splitStack(count);
                if(slots[index].getCount() == 0){
                    slots[index] = ItemStack.EMPTY;
                }
                this.markDirty();
                return stackAt;
            }
        }
        return null;
    }

    public void incrStackSize(int index, int count) {
        if(!slots[index].isEmpty()) {
            this.setInventorySlotContents(index, new ItemStack(slots[index].getItem(), slots[index].getCount() + count, slots[index].getItemDamage()));
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.slots[index] = ItemStack.EMPTY;
    }


    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(index >= 0 && index <= this.slots.length)
            this.slots[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.getPos().getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64 && !this.isInvalid() && this.world.getTileEntity(this.pos) == this;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName(){
        return this.invName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(this.invName);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, NBTSaveType type) {
        if(type == NBTSaveType.SAVE){
            if(this.slots.length > 0){
                NBTTagList tagList = new NBTTagList();
                for(int currentIndex = 0; currentIndex < slots.length; currentIndex++){
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setByte("Slot", (byte)currentIndex);
                    if(!slots[currentIndex].isEmpty()){
                        slots[currentIndex].writeToNBT(tagCompound);
                    }
                    tagList.appendTag(tagCompound);
                }
                compound.setTag("Items", tagList);
            }
        }
        super.writeToNBT(compound, type);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, NBTSaveType type) {
        super.readFromNBT(compound, type);
        if(type == NBTSaveType.SAVE){
            if(this.slots.length > 0){
                NBTTagList tagList = compound.getTagList("Items", 10);
                for(int i = 0; i < tagList.tagCount(); i++){
                    NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                    byte slotIndex = tagCompound.getByte("Slot");
                    if(slotIndex >= 0 && slotIndex < slots.length){
                        slots[slotIndex] = new ItemStack(tagCompound);
                    }
                }
            }
        }
    }

    public int getNextFreeSlot(int slotIDMin, int slotIDMax){
        for(int i = slotIDMin; i <= slotIDMax; i++){
            ItemStack stack = getStackInSlot(i);
            if(stack.isEmpty()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null){
            return (T) this.invWrappers[facing.getIndex()];
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        return this.getCapability(capability, facing) != null;
    }
}
