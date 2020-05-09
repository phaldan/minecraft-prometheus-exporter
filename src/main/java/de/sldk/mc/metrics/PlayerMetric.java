package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

public abstract class PlayerMetric extends Metric {

    private final MinecraftApi server;

    public PlayerMetric(Collector collector, CollectorRegistry registry, MinecraftApi server) {
        super(collector, registry);
        this.server = server;
    }

    @Override
    public final void doCollect() {
        for (MinecraftPlayer player: server.getPlayers()) {
            collect(player);
        }
    }

    protected abstract void collect(MinecraftPlayer player);
}
