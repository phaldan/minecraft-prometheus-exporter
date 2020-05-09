package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

public abstract class Metric {

    private final static String COMMON_PREFIX = "mc_";

    private final Collector collector;
    private final CollectorRegistry registry;

    protected Metric(Collector collector, CollectorRegistry registry) {
        this.collector = collector;
        this.registry = registry;
    }

    public void doCollect() {
    }

    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
        registry.register(collector);
    }

    public void disable() {
        registry.unregister(collector);
    }
}
