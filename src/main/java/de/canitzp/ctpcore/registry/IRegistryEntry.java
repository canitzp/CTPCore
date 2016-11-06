package de.canitzp.ctpcore.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implement this to an object and override the methods correct and you can register it via MCRegistry.register
 * @author canitzp
 */
public interface IRegistryEntry {

    IRegistryEntry[] getRegisterElements();

    ResourceLocation getRegisterName();

    void onRegister(IRegistryEntry[] otherEntries);

    void ownRegistry();

    @SideOnly(Side.CLIENT)
    void registerRenderer();

}
