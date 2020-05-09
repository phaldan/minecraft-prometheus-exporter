package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class PlayersTotal extends Metric {

    private static final Gauge PLAYERS = Gauge.build()
            .name(prefix("players_total"))
            .help("Unique players (online + offline)")
            .create();

    private final MinecraftApi server;

    public PlayersTotal(CollectorRegistry registry, MinecraftApi server) {
        super(PLAYERS, registry);
        this.server = server;
    }

    @Override
    public void doCollect() {
        PLAYERS.set(server.countPlayers());
    }
}
