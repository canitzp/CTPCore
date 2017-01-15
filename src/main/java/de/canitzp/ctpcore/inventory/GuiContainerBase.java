package de.canitzp.ctpcore.inventory;

import de.canitzp.ctpcore.base.TileEntityBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public abstract class GuiContainerBase<T extends TileEntityBase> extends GuiContainer{

    public T tile;

    public GuiContainerBase(ContainerBase<T> container){
        super(container);
        this.tile = container.tile;
    }

    protected void bindTexture(ResourceLocation res){
        this.mc.getTextureManager().bindTexture(res);
    }

    protected void clearColor(){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void drawBackgroundLocation(ResourceLocation loc){
        bindTexture(loc);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

}
