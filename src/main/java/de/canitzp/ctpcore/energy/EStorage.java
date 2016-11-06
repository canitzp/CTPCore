package de.canitzp.ctpcore.energy;

import de.canitzp.ctpcore.Compat;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * This class is for energy storages for the internal energy of ctpcore.
 * It has the same factor like RF, Tesla, FU and can be simply converted
 * between every energy unit.
 * @author canitzp
 */
public class EStorage {

    public int capacity;
    public int maxTransfer;
    public int currentStored;

    public EStorage(int capacity, int maxTransfer){
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public int getCurrentStored(){
        return this.currentStored;
    }

    public int getMaxTransfer(){
        return this.maxTransfer;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void setTransfer(int maxTransfer){
        this.maxTransfer = maxTransfer;
    }

    public int receiveEnergy(int energy, boolean simulate) {
        int energyReceived = Math.min(this.capacity - this.currentStored, Math.min(this.maxTransfer, energy));
        if (!simulate) {
            this.currentStored += energyReceived;
        }
        return energyReceived;
    }

    public int extractEnergy(int energy, boolean simulate) {
        int energyExtracted = Math.min(this.currentStored, Math.min(this.maxTransfer, energy));
        if (!simulate) {
            this.currentStored -= energyExtracted;
        }
        return energyExtracted;
    }

    public void writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("CTPEnergy", this.currentStored);
    }

    public void readFromNBT(NBTTagCompound nbt){
        this.currentStored = nbt.getInteger("CTPEnergy");
    }

    @Optional.Method(modid = Compat.TESLA_MODID)
    public <T> T getTeslaCapability(Capability<T> cap){
        if(cap == TeslaCapabilities.CAPABILITY_HOLDER || cap == TeslaCapabilities.CAPABILITY_HOLDER || cap == TeslaCapabilities.CAPABILITY_PRODUCER){
            return (T) new TeslaWrapper(this);
        }
        return null;
    }

    public IEnergyStorage getForgeEnergy(){
        EnergyStorage storage = new EnergyStorage(this.capacity, this.maxTransfer);
        storage.receiveEnergy(this.getCurrentStored(), false);
        return storage;
    }
}
