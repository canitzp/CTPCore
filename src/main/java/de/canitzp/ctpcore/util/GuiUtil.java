package de.canitzp.ctpcore.util;

import de.canitzp.ctpcore.base.TileEntityEnergy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiUtil {

    public static boolean isMouseInRange(int mouseX, int mouseY, int guiLeft, int guiTop, int x, int y, int width, int height){
        return mouseX - guiLeft >= x && mouseX - guiLeft <= x + width && mouseY - guiTop >= y && mouseY - guiTop <= y + height;
    }

    public static int getEnergyScale(TileEntityEnergy tile, int height){
        int capacity = tile.getMaxEnergy();
        return capacity > 0 ? Math.round(tile.getStoredEnergy() * height / (capacity * 1.0F)) : 0;
    }

    public static void drawEnergyBar(TileEntityEnergy tile, GuiContainer gui, int x, int y, int textX, int textY, int width, int height){
        int scale = getEnergyScale(tile, height);
        if(scale > 0){
            gui.drawTexturedModalRect(x, y + height - scale, textX, textY + height - scale, width, scale);
        }
    }

}
