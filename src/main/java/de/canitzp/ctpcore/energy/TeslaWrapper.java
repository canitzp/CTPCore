package de.canitzp.ctpcore.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;

/**
 * @author canitzp
 */
public class TeslaWrapper implements ITeslaHolder, ITeslaConsumer, ITeslaProducer {

    private EStorage storage;

    public TeslaWrapper(EStorage storage){
        this.storage = storage;
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return this.storage.receiveEnergy((int) power, simulated);
    }

    @Override
    public long getStoredPower() {
        return this.storage.getCurrentStored();
    }

    @Override
    public long getCapacity() {
        return this.storage.getCapacity();
    }

    @Override
    public long takePower(long power, boolean simulated) {
        return this.storage.extractEnergy((int) power, simulated);
    }
}
