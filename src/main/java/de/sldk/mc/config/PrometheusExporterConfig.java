package de.sldk.mc.config;

import de.sldk.mc.MetricRegistry;
import de.sldk.mc.metrics.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrometheusExporterConfig {

    public static final PluginConfig<String> HOST = new PluginConfig<>("host");
    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port");

    private final List<MetricConfig> metrics = new ArrayList<>();

    private final Plugin bukkitPlugin;

    private final MetricRegistry registry;

    public PrometheusExporterConfig(Plugin bukkitPlugin, Map<String, Metric> metrics, MetricRegistry registry) {
        this.bukkitPlugin = bukkitPlugin;
        this.registry = registry;
        metrics.forEach(this::metricConfig);
    }

    private void metricConfig(String key, Metric metric) {
        MetricConfig metricConfig = new MetricConfig(key, metric);
        this.metrics.add(metricConfig);
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

            registry.register(metric);
        });
    }

    public <T> T get(PluginConfig<T> config) {
        return config.get(bukkitPlugin.getConfig());
    }
}
