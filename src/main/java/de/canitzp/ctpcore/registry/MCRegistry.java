package de.canitzp.ctpcore.registry;

import com.google.common.collect.Lists;
import de.canitzp.ctpcore.CTPCore;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main register class to add all IRegistryEntries to the game.
 * @author canitzp
 */
public class MCRegistry {

    /**
     * This method is to register more than one thing IRegistryEntry the the same call
     * @param entries The IRegistryEntries you want to register
     * @param <T> A object that implements IRegistryEntry
     * @return The same objects you gave it
     */
    public static <T extends IRegistryEntry> T[] register(T... entries){
        for(T entry : entries){
            register(entry);
        }
        return entries;
    }

    /**
     * This is the main register method for all IRegistryEntries.
     * @param entry The IRegistryEntry you want to register
     * @param <T> A object that implements IRegistryEntry
     * @return The same object you gave it
     */
    public static <T extends IRegistryEntry> T register(T entry) {
        IRegistryEntry[] entries = entry.getRegisterElements();
        for (IRegistryEntry reg : entries) {
            if (reg instanceof IForgeRegistryEntry) {
                ((IForgeRegistryEntry) reg).setRegistryName(reg.getRegisterName());
                GameRegistry.register((IForgeRegistryEntry<?>) reg);
                reg.onRegister(entries);
                if(CTPCore.side.isClient()){
                    reg.registerRenderer();
                }
            } else {
                reg.ownRegistry();
            }
        }
        return entry;
    }

    public static boolean processRegistering(Class<?>... registerClasses){
        for(Class<?> registerClass : registerClasses){
            for(Field field : registerClass.getDeclaredFields()){
                if(field.isAnnotationPresent(Register.class)){
                    try {
                        Object o = field.getType().newInstance();
                        if(o instanceof IRegistryEntry){
                            MCRegistry.register((IRegistryEntry) o);
                            field.set(o, null);
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
