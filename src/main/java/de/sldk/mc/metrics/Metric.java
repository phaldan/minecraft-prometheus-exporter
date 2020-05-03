package de.sldk.mc.metrics;

import io.prometheus.client.Collector;

public abstract class Metric extends Collector {

    private final static String COMMON_PREFIX = "mc_";

    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
    }

    public void disable() {
    }
}
