package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.List;

public class PlayersTotal extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("players_total"))
            .help("Unique players (online + offline)")
            .create();

    private final MinecraftApi server;

    public PlayersTotal(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        collector.set(server.countPlayers());
        return collector.collect();
    }
}
