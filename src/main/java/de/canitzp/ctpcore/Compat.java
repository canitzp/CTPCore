package de.canitzp.ctpcore;

import net.minecraftforge.fml.common.Loader;

/**
 * @author canitzp
 */
public class Compat {

    public static final String IC2_MODID = "ic2";
    public static final String TESLA_MODID = "tesla";

    public static final boolean isIC2 = Loader.isModLoaded(IC2_MODID);
    public static final boolean isTesla = Loader.isModLoaded(TESLA_MODID);

}
