package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class PlayersTotal extends Metric {

    private static final Gauge PLAYERS = Gauge.build()
            .name(prefix("players_total"))
            .help("Unique players (online + offline)")
            .create();

    private final MinecraftApi server;

    public PlayersTotal(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, PLAYERS, registry);
        this.server = server;
    }

    @Override
    public void doCollect() {
        PLAYERS.set(server.countPlayers());
    }
}
