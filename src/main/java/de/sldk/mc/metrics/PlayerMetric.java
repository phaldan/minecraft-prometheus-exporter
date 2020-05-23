package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

public abstract class PlayerMetric extends Metric {

    private final MinecraftApi server;

    public PlayerMetric(Plugin plugin, Collector collector, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, collector, registry);
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
