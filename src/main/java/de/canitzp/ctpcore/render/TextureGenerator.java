package de.canitzp.ctpcore.render;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sun.jndi.rmi.registry.RegistryContext;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class TextureGenerator{

    /**
     * The first Location is the RegistryName of the Item/Block and the second the file with .png extension
     */
    public static Map<ResourceLocation, ResourceLocation> relatedTextureLocations = new HashMap<>();

    static {
        System.out.println("init");
        relatedTextureLocations.put(new ResourceLocation("minecraft:stone"), null);
    }

    @SubscribeEvent
    public void textureStich(TextureStitchEvent.Pre event){
        TextureMap textureMap = event.getMap();
        for(Map.Entry<ResourceLocation, ResourceLocation> entry : relatedTextureLocations.entrySet()){
            if(ForgeRegistries.BLOCKS.containsKey(entry.getKey())){
                Block block = ForgeRegistries.BLOCKS.getValue(entry.getKey());
                //TODO Block
            } else if(ForgeRegistries.ITEMS.containsKey(entry.getKey())){
                Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
                System.out.println(item);
                String name = "minecraft:textures/items/acacia_boat.png";
                TextureAtlasSprite sprite = textureMap.getAtlasSprite(name);
                if(sprite == null){
                    sprite = new Sprite(name);
                    textureMap.setTextureEntry(sprite);
                }
            }
        }
    }

    @SubscribeEvent
    public void bakeModels(ModelBakeEvent event){
        TextureMap textureMap = event.getModelManager().getTextureMap();
        IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        for(Map.Entry<ResourceLocation, ResourceLocation> entry : relatedTextureLocations.entrySet()){
            if(ForgeRegistries.BLOCKS.containsKey(entry.getKey())){
                Block block = ForgeRegistries.BLOCKS.getValue(entry.getKey());
                //TODO Block
            } else if(ForgeRegistries.ITEMS.containsKey(entry.getKey())){
                Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
                System.out.println(item);
                String textureName = "minecraft:textures/items/acacia_boat.png";
                TextureAtlasSprite sprite = textureMap.getAtlasSprite(textureName);

                ModelResourceLocation model = new ModelResourceLocation(Item.REGISTRY.getNameForObject(item), "inventory");
                ModelLoader.registerItemVariants(item, model);

                ItemLayerModel layerModel = new ItemLayerModel(ImmutableList.of(new ResourceLocation(textureName)));
                IBakedModel bakedModel = layerModel.bake(ItemLayerModel.INSTANCE.getDefaultState(), DefaultVertexFormats.ITEM, input -> sprite);
                ItemCameraTransforms transforms = getItemTransformer(false);
                ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(transforms);
                IPerspectiveAwareModel iPerspectiveAwareModel = new IPerspectiveAwareModel.MapWrapper(bakedModel, map);
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, model);
                modelRegistry.putObject(model, iPerspectiveAwareModel);
            }
        }
    }

    private ItemCameraTransforms getItemTransformer(boolean handheld){
        try{
            String s = handheld ? "minecraft:models/item/handheld" : "minecraft:models/item/generated";
            ResourceLocation file = new ResourceLocation(s + ".json");
            IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(file);
            Reader reader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8));
            return ModelBlock.deserialize(reader).getAllTransforms();
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private class Sprite extends TextureAtlasSprite{
        protected Sprite(String spriteName){
            super(spriteName);
        }
    }
}
