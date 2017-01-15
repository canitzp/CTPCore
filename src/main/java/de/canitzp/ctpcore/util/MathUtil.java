package de.canitzp.ctpcore.util;

/**
 * @author canitzp
 */
public class MathUtil{

    public static float round(float number, int decimalpoints){
        long multi = Math.round(Math.pow(10, decimalpoints));
        return Math.round(number * multi) / multi;
    }

}
