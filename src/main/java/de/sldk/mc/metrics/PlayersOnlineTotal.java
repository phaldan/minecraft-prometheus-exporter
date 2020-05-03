package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.List;

public class PlayersOnlineTotal extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online per world")
            .labelNames("world")
            .create();

    private final MinecraftApi server;

    public PlayersOnlineTotal(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        server.getWorlds().forEach(this::collect);
        return collector.collect();
    }

    private void collect(MinecraftWorld world) {
        collector.labels(world.getName()).set(world.countPlayers());
    }
}
