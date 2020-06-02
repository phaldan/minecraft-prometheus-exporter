package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public abstract class PlayerMetric extends Metric {

    public PlayerMetric(Plugin plugin, Collector collector, CollectorRegistry registry) {
        super(plugin, collector, registry);
    }

    @Override
    public final void doCollect() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            collect(player);
        }
    }

    protected abstract void collect(OfflinePlayer player);

    protected String getUid(OfflinePlayer player) {
        return player.getUniqueId().toString();
    }

    protected String getNameOrUid(OfflinePlayer player) {
        return player.getName() != null ? player.getName() : player.getUniqueId().toString();
    }

}
