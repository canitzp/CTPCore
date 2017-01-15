package de.canitzp.ctpcore.registry;

import de.canitzp.ctpcore.base.TileEntityBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class TileEntityProvider implements IRegistryEntry{

    private Class<? extends TileEntityBase> clazz;
    private ResourceLocation res;

    public TileEntityProvider(Class<? extends TileEntityBase> clazz, ResourceLocation res){
        this.clazz = clazz;
        this.res = res;
    }

    @Override
    public IRegistryEntry[] getRegisterElements(){
        return new IRegistryEntry[]{this};
    }

    @Override
    public ResourceLocation getRegisterName(){
        return this.res;
    }

    @Override
    public void onRegister(IRegistryEntry[] otherEntries){

    }

    @Override
    public void ownRegistry(){
        GameRegistry.registerTileEntity(this.clazz, this.res.toString());
    }

    @Override
    public void registerRenderer(){

    }
}
