package de.canitzp.ctpcore;

import com.google.common.io.Files;
import de.canitzp.ctpcore.registry.MCRegistry;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FilenameFilter;

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

    public static String isDevEnv = "true";

    public static final String CORE_NAME = "CTPCore";
    public static final String CORE_VERSION = "1.0.0";
    public static final String MINECRAFT_VERSION = "1.11.2";
    public static final int MINIMUM_FORGE_VERSION = 2092;

    public static final Logger logger = LogManager.getLogger(CORE_NAME);

    public static final Side side = FMLCommonHandler.instance().getSide();

    /**
     * A method to check if the library can be used without errors.
     * @param event The phase event when this method is called.
     *              {@link FMLPreInitializationEvent}
     *              {@link FMLInitializationEvent}
     *              {@link FMLPostInitializationEvent}
     * @throws NotCorrectEnvironmentException if the environment is not correct
     * @deprecated renew this method -> no automated library download
     */
    //TODO
    @Deprecated
    public static void checkEnvironment(@Nullable FMLStateEvent event) throws NotCorrectEnvironmentException {
        //check for devEnv:
        try {
            Class.forName("net.minecraft.world.World");
            isDevEnv = "true";
        } catch (ClassNotFoundException e) {
            isDevEnv = "false";
        }
        //Check for correct Minecraft and Forge Version:
        if(!ForgeVersion.mcVersion.equals(MINECRAFT_VERSION)){
            throw new NotCorrectEnvironmentException("Wrong Minecraft Version! Current:'" + ForgeVersion.mcVersion + "' Needed:'" + MINECRAFT_VERSION + "'");
        }
        if(ForgeVersion.getBuildVersion() < MINIMUM_FORGE_VERSION){
            throw new NotCorrectEnvironmentException("To low Forge Version! Minimum required:'" + MINIMUM_FORGE_VERSION + "'");
        }
        //Check for the correct folder:
        if(event != null){
            boolean correctLocation = false;
            if(event.getModState().equals(LoaderState.ModState.PREINITIALIZED)){
                FMLPreInitializationEvent pre = (FMLPreInitializationEvent) event;
                correctLocation = isCorrectFileLocation(pre.getModConfigurationDirectory());
            }

            if(!isDevEnv() && !correctLocation){
                throw new NotCorrectEnvironmentException("The location for the jar file of this library is incorrect!");
            }
        }
    }

    private static boolean isCorrectFileLocation(File configFolder){
        File mcVersionModFolder = new File(configFolder.getParent() + File.separator + "mods" + File.separator + MINECRAFT_VERSION);
        if(mcVersionModFolder.exists()){
            String[] files = mcVersionModFolder.list((dir, name) -> name.endsWith(".jar"));
            for(String file : files){
                if(file.toLowerCase().contains(CORE_NAME.toLowerCase())){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDevEnv(){
        return isDevEnv.equals("true");
    }

}
