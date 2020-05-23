package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

public abstract class WorldMetric extends Metric {

    private final MinecraftApi server;

    public WorldMetric(Plugin plugin, Collector collector, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, collector, registry);
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
