package de.sldk.mc;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class CoreModule {

    CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    MetricsController metricsController(Plugin bukkitPlugin, CollectorRegistry registry, MetricRegistry metricRegistry) {
        return new MetricsController(bukkitPlugin, registry, metricRegistry);
    }

    MetricService metricService(PrometheusExporterConfig config, MetricRegistry metricRegistry) {
        return new MetricService(config, metricRegistry);
    }

    PrometheusExporterConfig prometheusExporterConfig(MinecraftApi server, Map<String, Metric> metrics) {
        return new PrometheusExporterConfig(server, metrics);
    }

    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    MinecraftApi minecraftPluginAdapter(Plugin plugin) {
        return new MinecraftApi(plugin);
    }
}
