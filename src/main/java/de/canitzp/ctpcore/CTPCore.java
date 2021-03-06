package de.canitzp.ctpcore;

import de.canitzp.ctpcore.base.BlockBase;
import de.canitzp.ctpcore.event.TileEntityEvents;
import de.canitzp.ctpcore.inventory.CTPGuiHandler;
import de.canitzp.ctpcore.registry.MCRegistry;
import de.canitzp.ctpcore.sync.SyncTile;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The CTPCore isn't a standalone Mod.
 * It is a library that provides Minecraft
 * and MinecraftForge related things, like the
 * new Registry system {@link MCRegistry}.
 * It is possible to bundle it with the mod that
 * uses the methods, but it's better to check
 * if this library is present and if not to
 * automatic download it and save it to the
 * minecraft version folder in the mods folder.
 * @author canitzp
 * @since 1.0.0
 */
public class CTPCore {

    public static final String CORE_NAME = "CTPCore";
    public static final String CORE_VERSION = "1.0.0";
    public static final String MINECRAFT_VERSION = "1.11.2";
    public static final int MINIMUM_FORGE_VERSION = 2092;

    public static final Logger logger = LogManager.getLogger(CORE_NAME);
    public static SimpleNetworkWrapper network;
    public static final Side side = FMLCommonHandler.instance().getSide();

    public static final List<Object> modsLoadedWithCTP = new ArrayList<>();

    private static boolean alreadyInitialized;

    public static void init(Object mod, FMLInitializationEvent event){
        if(!isModRegistered(mod) && mod.getClass().isAnnotationPresent(Mod.class)){
            modsLoadedWithCTP.add(mod);
            NetworkRegistry.INSTANCE.registerGuiHandler(mod, CTPGuiHandler.INSTANCE);
            logger.info("Found CTP-Mod " + mod.getClass().getAnnotation(Mod.class).name());
        }
        if(!alreadyInitialized){
            alreadyInitialized = true;
            init(event);
        }
    }

    private static void init(FMLInitializationEvent event){
        network = NetworkRegistry.INSTANCE.newSimpleChannel(CORE_NAME + "Network");
        network.registerMessage(SyncTile.class, SyncTile.class, 0, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(TileEntityEvents.class);
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 10);
        for(Map.Entry<Block, String> entry : BlockBase.oreDicts.entrySet()){
            OreDictionary.registerOre(entry.getValue(), entry.getKey());
        }
    }

    public static boolean isModRegistered(Object mod){
        return modsLoadedWithCTP.contains(mod);
    }

}
