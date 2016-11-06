package de.canitzp.ctpcore;

/**
 * @author canitzp
 */
public class NotCorrectEnvironmentException extends Exception {

    public NotCorrectEnvironmentException(String reason) {
        super("Not the correct Minecraft/Minecraft Forge Environment for CTPCore. Reason: " + reason);
    }

}
