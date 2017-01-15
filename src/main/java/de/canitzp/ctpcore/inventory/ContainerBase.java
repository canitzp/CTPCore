package de.canitzp.ctpcore.inventory;

import de.canitzp.ctpcore.base.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.math.BlockPos;

/**
 * @author canitzp
 */
public abstract class ContainerBase<T extends TileEntityBase> extends Container{

    public T tile;
    public EntityPlayer player;

    public ContainerBase(EntityPlayer player, int x, int y, int z){
        this.player = player;
        this.tile = (T) player.getEntityWorld().getTileEntity(new BlockPos(x, y, z));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return player.getDistanceSq((double)this.tile.getPos().getX() + 0.5D, (double)this.tile.getPos().getY() + 0.5D, (double)this.tile.getPos().getZ() + 0.5D) <= 64.0D;
    }

    protected void addPlayerInventorySlots(int x, int y){
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 9; ++j){
                this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
    }

    protected void addPlayerHotbarSlots(int x, int y){
        for (int k = 0; k < 9; ++k){
            this.addSlotToContainer(new Slot(this.player.inventory, k, x + k * 18, y));
        }
    }

}
