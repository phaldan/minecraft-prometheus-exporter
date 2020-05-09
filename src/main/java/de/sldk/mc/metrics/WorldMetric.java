package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

public abstract class WorldMetric extends Metric {

    private final MinecraftApi server;

    public WorldMetric(Collector collector, CollectorRegistry registry, MinecraftApi server) {
        super(collector, registry);
        this.server = server;
    }

    @Override
    public final void doCollect() {
        for (MinecraftWorld world : server.getWorlds()) {
            collect(world);
        }
    }

    protected abstract void collect(MinecraftWorld world);
}
