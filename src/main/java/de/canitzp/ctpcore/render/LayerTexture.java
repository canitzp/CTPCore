package de.canitzp.ctpcore.render;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author canitzp
 */
public class LayerTexture extends AbstractTexture{

    private ResourceLocation[] locations;

    public LayerTexture(@Nonnull ResourceLocation... locations){
        this.locations = locations;
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException{
        this.deleteGlTexture();
        BufferedImage image = null;
        for(ResourceLocation location : this.locations){
            IResource resource = null;
            try{
                if(location != null){
                    resource = resourceManager.getResource(location);
                    BufferedImage image1 = TextureUtil.readBufferedImage(resource.getInputStream());
                    if(image == null){
                        image = new BufferedImage(image1.getWidth(), image1.getHeight(), 2);
                    }
                    image.getGraphics().drawImage(image1, 0, 0, null);
                }
                continue;
            } catch(IOException e){
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(resource);
            }
            return;
        }
        if(image != null){
            TextureUtil.uploadTextureImage(this.getGlTextureId(), image);
        }
    }
}
