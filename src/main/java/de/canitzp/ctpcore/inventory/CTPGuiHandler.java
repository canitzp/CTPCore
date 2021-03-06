package de.canitzp.ctpcore.inventory;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class CTPGuiHandler implements IGuiHandler {

    public static CTPGuiHandler INSTANCE = new CTPGuiHandler();

    private static Map<Integer, Class<? extends GuiScreen>> guis = new HashMap<>();
    private static Map<Integer, Class<? extends Container>> container = new HashMap<>();

    public static int addGui(Class<? extends GuiScreen> gui){
        int ID = guis.size();
        guis.put(ID, gui);
        return ID;
    }

    public static int addGuiContainer(Class<? extends GuiScreen> gui, Class<? extends Container> con){
        int ID = addGui(gui);
        container.put(ID, con);
        return ID;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(container.containsKey(ID)){
            Class<? extends Container> con = container.get(ID);
            if(con != null){
                try {
                    return con.getDeclaredConstructor(EntityPlayer.class, int.class, int.class, int.class).newInstance(player, x, y, z) ;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    try {
                        return con.getDeclaredConstructor(EntityPlayer.class).newInstance(player);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
                        try {
                            return con.getDeclaredConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(guis.containsKey(ID)){
            Class<? extends GuiScreen> gui = guis.get(ID);
            if(gui != null){
                try {
                    return gui.getDeclaredConstructor(EntityPlayer.class, int.class, int.class, int.class).newInstance(player, x, y, z) ;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    try {
                        return gui.getDeclaredConstructor(EntityPlayer.class).newInstance(player);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
                        try {
                            return gui.getDeclaredConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
