package de.sldk.mc.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PlayersTotal extends Metric {

    private static final Gauge PLAYERS = Gauge.build()
            .name(prefix("players_total"))
            .help("Unique players (online + offline)")
            .create();

    public PlayersTotal(Plugin plugin, CollectorRegistry registry) {
        super(plugin, PLAYERS, registry);
    }

    @Override
    public void doCollect() {
        PLAYERS.set(Bukkit.getOfflinePlayers().length);
    }
}
