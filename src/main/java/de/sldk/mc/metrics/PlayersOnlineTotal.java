package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class PlayersOnlineTotal extends WorldMetric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online per world")
            .labelNames("world")
            .create();

    public PlayersOnlineTotal(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, PLAYERS_ONLINE, registry, server);
    }

    @Override
    protected void collect(MinecraftWorld world) {
        PLAYERS_ONLINE.labels(world.getName()).set(world.countPlayers());
    }
}
