package de.canitzp.ctpcore.sync;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author canitzp
 */
public class SyncTile implements IMessage, IMessageHandler<SyncTile, IMessage>{

    private NBTTagCompound sync;
    private BlockPos tilePos;

    public SyncTile(){}

    public SyncTile(NBTTagCompound sync, BlockPos tilePos){
        this.sync = sync;
        this.tilePos = tilePos;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        try{
            PacketBuffer pack = new PacketBuffer(buf);
            this.sync = pack.readCompoundTag();
            this.tilePos = pack.readBlockPos();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf){
        PacketBuffer pack = new PacketBuffer(buf);
        pack.writeCompoundTag(this.sync);
        pack.writeBlockPos(this.tilePos);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(SyncTile message, MessageContext ctx){
        Minecraft.getMinecraft().addScheduledTask(() -> {
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.tilePos);
            if(tile != null && tile instanceof ISyncable){
                ((ISyncable) tile).receiveData(message.sync);
            }
        });
        return null;
    }
}
