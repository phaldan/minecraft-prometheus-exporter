package de.sldk.mc.config;

import de.sldk.mc.MetricRegistry;
import de.sldk.mc.metrics.*;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class PrometheusExporterConfig {

    public static final PluginConfig<String> HOST = new PluginConfig<>("host");
    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port");
    private final List<MetricConfig> metrics;

    private final Plugin bukkitPlugin;

    public PrometheusExporterConfig(Plugin bukkitPlugin, CollectorRegistry registry) {
        this.bukkitPlugin = bukkitPlugin;
        metrics = Arrays.asList(
            metricConfig("entities_total", new Entities(bukkitPlugin, registry)),
            metricConfig("villagers_total", new Villagers(bukkitPlugin, registry)),
            metricConfig("loaded_chunks_total", new LoadedChunks(bukkitPlugin, registry)),
            metricConfig("jvm_memory", new Memory(bukkitPlugin, registry)),
            metricConfig("players_online_total", new PlayersOnlineTotal(bukkitPlugin, registry)),
            metricConfig("players_total", new PlayersTotal(bukkitPlugin, registry)),
            metricConfig("tps", new Tps(bukkitPlugin, registry)),

            metricConfig("jvm_threads", new ThreadsWrapper(bukkitPlugin, registry)),
            metricConfig("jvm_gc", new GarbageCollectorWrapper(bukkitPlugin, registry)),

            metricConfig("tick_duration_median", new TickDurationMedianCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_average", new TickDurationAverageCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_min", new TickDurationMinCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_max", new TickDurationMaxCollector(bukkitPlugin, registry)),

            metricConfig("player_online", new PlayerOnline(bukkitPlugin, registry)),
            metricConfig("player_statistic", new PlayerStatistics(bukkitPlugin, registry)));
    }

    private MetricConfig metricConfig(String key, Metric metric) {
        return new MetricConfig(key, metric);
    }

    public void loadDefaultsAndSave() {
        bukkitPlugin.saveDefaultConfig();
        bukkitPlugin.getConfig().options().copyDefaults(false);
        bukkitPlugin.saveConfig();
    }

    public void enableConfiguredMetrics() {
        metrics.forEach(metricConfig -> {
            Metric metric = metricConfig.getMetric();
            Boolean enabled = get(metricConfig);

            if (Boolean.TRUE.equals(enabled)) {
                metric.enable();
            }

            bukkitPlugin.getLogger().fine("Metric " + metric.getClass().getSimpleName() + " enabled: " + enabled);

            MetricRegistry.getInstance().register(metric);
        });
    }

    public <T> T get(PluginConfig<T> config) {
        return config.get(bukkitPlugin.getConfig());
    }
}
