package de.sldk.mc.config;

import de.sldk.mc.MetricRegistry;
import de.sldk.mc.metrics.*;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class PrometheusExporterConfig {

    public static final PluginConfig<String> HOST = new PluginConfig<>("host", "localhost");
    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port", 9225);
    private final List<MetricConfig> metrics;

    private final Plugin bukkitPlugin;

    public PrometheusExporterConfig(Plugin bukkitPlugin, CollectorRegistry registry) {
        this.bukkitPlugin = bukkitPlugin;
        metrics = Arrays.asList(
            metricConfig("entities_total", true, new Entities(bukkitPlugin, registry)),
            metricConfig("villagers_total", true, new Villagers(bukkitPlugin, registry)),
            metricConfig("loaded_chunks_total", true, new LoadedChunks(bukkitPlugin, registry)),
            metricConfig("jvm_memory", true, new Memory(bukkitPlugin, registry)),
            metricConfig("players_online_total", true, new PlayersOnlineTotal(bukkitPlugin, registry)),
            metricConfig("players_total", true, new PlayersTotal(bukkitPlugin, registry)),
            metricConfig("tps", true, new Tps(bukkitPlugin, registry)),

            metricConfig("jvm_threads", true, new ThreadsWrapper(bukkitPlugin, registry)),
            metricConfig("jvm_gc", true, new GarbageCollectorWrapper(bukkitPlugin, registry)),

            metricConfig("tick_duration_median", true, new TickDurationMedianCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_average", true, new TickDurationAverageCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_min", false, new TickDurationMinCollector(bukkitPlugin, registry)),
            metricConfig("tick_duration_max", true, new TickDurationMaxCollector(bukkitPlugin, registry)),

            metricConfig("player_online", false, new PlayerOnline(bukkitPlugin, registry)),
            metricConfig("player_statistic", false, new PlayerStatistics(bukkitPlugin, registry)));
    }

    private MetricConfig metricConfig(String key, boolean defaultValue, Metric metric) {
        return new MetricConfig(key, defaultValue, metric);
    }

    public void loadDefaultsAndSave() {
        FileConfiguration configFile = bukkitPlugin.getConfig();

        PrometheusExporterConfig.HOST.setDefault(configFile);
        PrometheusExporterConfig.PORT.setDefault(configFile);
        metrics.forEach(metric -> metric.setDefault(configFile));

        configFile.options().copyDefaults(true);

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
