package de.canitzp.ctpcore.render;

import de.canitzp.ctpcore.base.FluidBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class RenderingRegistry {

    public static void addRenderer(Block block, int meta, ModelResourceLocation location){
        Item itemBlock = Item.getItemFromBlock(block);
        if(itemBlock != Items.AIR){
            addRenderer(itemBlock, meta, location);
        }
    }

    public static void addRenderer(Block block, ModelResourceLocation location){
        addRenderer(block, 0, location);
    }

    public static void addRenderer(Block block, int meta){
        addRenderer(block, meta, getModelResourceLocation(block));
    }

    public static void addRenderer(Block block){
        addRenderer(block, 0);
    }

    public static void addRenderer(Item item, int meta, ModelResourceLocation location){
        if(item != null){
            if(location != null){
                ModelLoader.setCustomModelResourceLocation(item, meta, location);
            } else {
                throw new NullPointerException("The model resource location for the item '" + item.getRegistryName() + "' (" + item + ") does not exist!");
            }
        } else {
            throw new NullPointerException();
        }
    }

    public static void addRenderer(Item item, ModelResourceLocation location){
        addRenderer(item, 0, location);
    }

    public static void addRenderer(Item item, int meta){
        addRenderer(item, meta, getModelResourceLocation(item));
    }

    public static void addRenderer(Item item){
        addRenderer(item, 0);
    }

    public static void addRenderer(FluidBase fluid){

    }

    private static ModelResourceLocation getModelResourceLocation(IForgeRegistryEntry entry){
        return entry != null && entry.getRegistryName() != null ? new ModelResourceLocation(entry.getRegistryName(), "inventory") : null;
    }

}
