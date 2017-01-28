package de.canitzp.ctpcore.base;

import de.canitzp.ctpcore.util.NBTSaveType;
import de.canitzp.ctpcore.util.NBTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileEntityBase extends TileEntity{

    public TileEntityBase(){}

    @Deprecated // No constructor needed anymore
    public TileEntityBase(ResourceLocation resource){}

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
        if(type == NBTSaveType.SAVE || type == NBTSaveType.DROP_BLOCK){
            if(this.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)){
                compound.merge(NBTUtil.writeInventory(this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)));
            }
        }
    }

    public void readFromNBT(NBTTagCompound compound, NBTSaveType type){
        if(type == NBTSaveType.SAVE || type == NBTSaveType.DROP_BLOCK){
            if(this.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)){
                NBTUtil.readInventory(compound, this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
            }
        }
    }

    public void syncToClient(){
        for(EntityPlayer player : this.getWorld().playerEntities){
            if(player instanceof EntityPlayerMP && canSync((EntityPlayerMP) player)){
                BlockPos pos = this.getPos();
                if(player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64){
                    ((EntityPlayerMP) player).connection.sendPacket(this.getUpdatePacket());
                }
            }
        }
    }

    public void spawnParticle(EnumParticleTypes type, double x, double y, double z, int amount, double speed, int[] args){
        if(!this.world.isRemote){
            ((WorldServer)this.world).spawnParticle(type, x, y, z, amount,0, 0, 0, speed, args);
        }
    }

    @Deprecated
    public boolean canSync(){
        return true;
    }

    public boolean canSync(EntityPlayerMP player){
        return true;
    }
}
