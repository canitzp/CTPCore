package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.registry.IRegistryEntry;
import de.canitzp.ctpcore.util.NBTSaveType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class TileEntityBase extends TileEntity implements IRegistryEntry{

    private static List<Class<? extends TileEntity>> registered = new ArrayList<>();

    private ResourceLocation resource;

    public TileEntityBase(ResourceLocation resource){
        this.resource = resource;
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound, NBTSaveType.SYNC);
        return new SPacketUpdateTileEntity(getPos(), 0, compound);
    }

    @Override
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if(pkt != null){
            this.readFromNBT(pkt.getNbtCompound(), NBTSaveType.SYNC);
        }
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        this.writeToNBT(compound, NBTSaveType.SAVE);
        return compound;
    }

    @Override
    public final void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        this.readFromNBT(compound, NBTSaveType.SAVE);
    }

    public void writeToNBT(NBTTagCompound compound, NBTSaveType type){

    }

    public void readFromNBT(NBTTagCompound compound, NBTSaveType type){

    }

    public void syncToClient(){
        for(EntityPlayer player : this.getWorld().playerEntities){
            if(player instanceof EntityPlayerMP){
                BlockPos pos = this.getPos();
                if(player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64){
                    ((EntityPlayerMP)player).connection.sendPacket(this.getUpdatePacket());
                }
            }
        }
    }

    @Override
    public IRegistryEntry[] getRegisterElements() {
        return new IRegistryEntry[]{this};
    }

    @Override
    public ResourceLocation getRegisterName() {
        return this.resource;
    }

    @Override
    public void onRegister(IRegistryEntry[] otherEntries) {

    }

    @Override
    public void ownRegistry() {
        if(!registered.contains(this.getClass())){
            GameRegistry.registerTileEntity(this.getClass(), this.resource.getResourceDomain() + this.resource.getResourcePath());
            registered.add(this.getClass());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer() {

    }
}
