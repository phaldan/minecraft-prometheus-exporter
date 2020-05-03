package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

public abstract class Metric {

    private final static String COMMON_PREFIX = "mc_";

    private final Collector collector;

    protected Metric(Collector collector, CollectorRegistry registry) {
        this.collector = collector;
    }

    public void doCollect() {
    }

    public Collector getCollector() {
        return collector;
    }

    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
    }

    public void disable() {
    }
}
