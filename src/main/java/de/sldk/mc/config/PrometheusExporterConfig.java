package de.sldk.mc.config;

import de.sldk.mc.logging.Logger;
import de.sldk.mc.logging.LoggerFactory;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.server.MinecraftApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrometheusExporterConfig {

    public static final PluginConfig<String> HOST = new PluginConfig<>("host");
    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port");

    private final Logger logger = LoggerFactory.getLogger();

    private final List<MetricConfig> metrics = new ArrayList<>();

    private final MinecraftApi server;

    public PrometheusExporterConfig(MinecraftApi server, Map<String, Metric> metrics) {
        this.server = server;
        metrics.forEach(this::metricConfig);
    }

    private void metricConfig(String key, Metric metric) {
        MetricConfig metricConfig = new MetricConfig(key, metric);
        this.metrics.add(metricConfig);
    }

    public void loadDefaultsAndSave() {
        server.loadDefaultConfig();
    }

    public List<Metric> getEnabledMetrics() {
        return metrics.stream()
                .filter(this::isMetricEnabled)
                .map(this::getEnabledMetric)
                .collect(Collectors.toList());
    }

    private boolean isMetricEnabled(MetricConfig config) {
        return config.get(server);
    }

    private Metric getEnabledMetric(MetricConfig config) {
        Metric metric = config.getMetric();
        logger.info("Enable metric {0} ({1})", metric.getClass().getSimpleName(), config.key);
        return metric;
    }

    public <T> T get(PluginConfig<T> config) {
        return config.get(server);
    }
}
