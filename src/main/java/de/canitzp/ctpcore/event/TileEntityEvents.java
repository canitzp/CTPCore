package de.canitzp.ctpcore.event;

import de.canitzp.ctpcore.CTPCore;
import de.canitzp.ctpcore.sync.ISyncable;
import de.canitzp.ctpcore.sync.SyncTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * @author canitzp
 */
public class TileEntityEvents{

    @SubscribeEvent
    public static void onTileUpdate(TickEvent.WorldTickEvent event){
        if(!event.world.isRemote){
            for(TileEntity tile : event.world.tickableTileEntities){
                if(tile instanceof ISyncable){
                    int delay = ((ISyncable) tile).getSyncTimeInTicks();
                    if(delay >= 0 && (delay == 0 || event.world.getTotalWorldTime() % delay == 0)){
                        CTPCore.network.sendToAllAround(new SyncTile(((ISyncable) tile).getSyncableData(), tile.getPos()), new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 64));
                    }
                }
            }
        }
    }

}
