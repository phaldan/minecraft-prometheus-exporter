package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.List;

public class PlayerOnline extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("player_online"))
            .help("Online state by player name")
            .labelNames("name", "uid")
            .create();

    private final MinecraftApi server;

    public PlayerOnline(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        server.getPlayers().forEach(this::collect);
        return collector.collect();
    }

    private void collect(MinecraftPlayer player) {
        collector.labels(player.getName(), player.getUniqueId()).set(player.isOnline() ? 1 : 0);
    }
}
