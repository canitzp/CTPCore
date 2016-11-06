package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.Compat;
import de.canitzp.ctpcore.energy.EStorage;
import de.canitzp.ctpcore.util.NBTSaveType;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

/**
 * @author canitzp
 */
//TODO make energy storage for every side -> problem with industrial craft
@Optional.Interface(modid = Compat.IC2_MODID, iface = "ic2.api.tile.IEnergyStorage")
public class TileEntityEnergy extends TileEntityInventory implements IEnergyStorage{

    public static final int EU_TO_RF_CONVERSION_RATE = 4;

    protected EStorage storage;

    public TileEntityEnergy(ResourceLocation resource, int slotAmount, int capacity, int maxTransfer) {
        super(resource, slotAmount);
        this.storage = new EStorage(capacity, maxTransfer);
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public int getStored() {
        return this.storage.getCurrentStored() / EU_TO_RF_CONVERSION_RATE;
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public void setStored(int i) {

    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public int addEnergy(int i) {
        return this.storage.receiveEnergy(i * EU_TO_RF_CONVERSION_RATE, false);
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public int getCapacity() {
        return this.storage.getCapacity() / EU_TO_RF_CONVERSION_RATE;
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public int getOutput() {
        return this.storage.getMaxTransfer() / EU_TO_RF_CONVERSION_RATE;
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public double getOutputEnergyUnitsPerTick() {
        return getOutput();
    }

    @Optional.Method(modid = Compat.IC2_MODID)
    @Deprecated
    @Override
    public boolean isTeleporterCompatible(EnumFacing enumFacing) {
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(Compat.isTesla){
            T cap = this.storage.getTeslaCapability(capability);
            if(cap != null){
                return cap;
            }
        }
        if(capability == CapabilityEnergy.ENERGY){
            return (T) this.storage.getForgeEnergy();
        }
        return super.getCapability(capability, facing);
    }

    public int receiveEnergy(int energy, boolean simulate){
        return this.storage.receiveEnergy(energy, simulate);
    }

    public int extractEnergy(int energy, boolean simulate){
        return this.storage.extractEnergy(energy, simulate);
    }

    public int getStoredEnergy(){
        return this.storage.getCurrentStored();
    }

    public int getMaxEnergy(){
        return this.storage.getCapacity();
    }



    @Override
    public void writeToNBT(NBTTagCompound compound, NBTSaveType type) {
        this.storage.writeToNBT(compound);
        super.writeToNBT(compound, type);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, NBTSaveType type) {
        this.storage.readFromNBT(compound);
        super.readFromNBT(compound, type);
    }
}
