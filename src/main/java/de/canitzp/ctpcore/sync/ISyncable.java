package de.canitzp.ctpcore.sync;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public interface ISyncable{

    NBTTagCompound getSyncableData();

    int getSyncTimeInTicks();

    void receiveData(NBTTagCompound data);

}
