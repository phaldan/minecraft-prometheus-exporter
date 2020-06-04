package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class PlayerOnline extends PlayerMetric {

    private static final Gauge PLAYERS_WITH_NAMES = Gauge.build()
            .name(prefix("player_online"))
            .help("Online state by player name")
            .labelNames("name", "uid")
            .create();

    public PlayerOnline(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, PLAYERS_WITH_NAMES, registry, server);
    }

    @Override
    public void collect(MinecraftPlayer player) {
        PLAYERS_WITH_NAMES.labels(player.getName(), player.getUniqueId()).set(player.isOnline() ? 1 : 0);
    }
}
