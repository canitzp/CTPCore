package de.canitzp.ctpcore.inventory;

import de.canitzp.ctpcore.base.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author canitzp
 */
public class FakeContainer<T extends TileEntityBase> extends ContainerBase<T> {

    public FakeContainer(EntityPlayer player, int x, int y, int z) {
        super(player, x, y, z);
    }

}
