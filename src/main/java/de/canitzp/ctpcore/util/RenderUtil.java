package de.canitzp.ctpcore.util;

import de.canitzp.ctpcore.property.ExtendedDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class RenderUtil{

    public static void transformToFront(EnumFacing facing, float xOffset, float yOffset, float zOffset){
        switch(facing){
            case DOWN: {
                GlStateManager.translate(xOffset, -0.5F + yOffset, zOffset);
                GlStateManager.rotate(-90F, 1F, 0, 0F);
                break;
            }
            case UP: {
                GlStateManager.translate(xOffset, 0.5F + yOffset, zOffset);
                GlStateManager.rotate(90F, 1F, 0, 0F);
                break;
            }
            case NORTH: {
                GlStateManager.translate(-xOffset, yOffset, -0.5F - zOffset);
                break;
            }
            case SOUTH: {
                GlStateManager.translate(xOffset, yOffset, 0.5F + zOffset);
                break;
            }
            case WEST: {
                GlStateManager.translate(-0.5F - zOffset, yOffset, xOffset);
                GlStateManager.rotate(90F, 0F, 1F, 0F);
                break;
            }
            case EAST: {
                GlStateManager.translate(0.5F + zOffset, yOffset, -xOffset);
                GlStateManager.rotate(90F, 0F, 1F, 0F);
                break;
            }
        }
    }

    public static void transformToFront(ExtendedDirection.ExtendedFacing facing, float xOffset, float yOffset, float zOffset){
        if(facing.getAlternativeFacing() != null){
            transformToFront(facing.getVanillaFacing(), 0, 0, 0);
        } else {
            transformToFront(facing.getVanillaFacing(), xOffset, yOffset, zOffset);
            return;
        }
        boolean up = facing.getVanillaFacing().equals(EnumFacing.UP);
        switch(facing.getAlternativeFacing()){
            case NORTH: {
                GlStateManager.translate(-xOffset, yOffset, -zOffset);
                break;
            }
            case SOUTH: {
                GlStateManager.translate(xOffset, -yOffset, -zOffset);
                GlStateManager.rotate(180, 0, 0, 1);
                break;
            }
            case WEST: {
                GlStateManager.translate(up ? xOffset: -xOffset, up ? yOffset : -yOffset, -zOffset);
                GlStateManager.rotate(up ? -90 : 90, 0, 0, 1);
                break;
            }
            case EAST: {
                GlStateManager.translate(up ? -xOffset: xOffset, up ? -yOffset : yOffset, -zOffset);
                GlStateManager.rotate(up ? 90 : -90, 0, 0, 1);
                break;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemInWorld(ItemStack stack){
        if(!stack.isEmpty()){
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

}
