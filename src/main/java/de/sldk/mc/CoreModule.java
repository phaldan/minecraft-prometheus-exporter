package de.sldk.mc;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;
import org.eclipse.jetty.server.Server;

import java.util.Map;

public class CoreModule {

    CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    MetricsController metricsController(MinecraftApi minecraftServer, CollectorRegistry registry, MetricRegistry metricRegistry) {
        return new MetricsController(minecraftServer, registry, metricRegistry, new Server());
    }

    MetricService metricService(
            PrometheusExporterConfig config,
            MetricRegistry metricRegistry,
            CollectorRegistry collectorRegistry,
            Map<String, Metric> metrics) {
        return new MetricService(config, metricRegistry, collectorRegistry, metrics);
    }

    PrometheusExporterConfig prometheusExporterConfig(MinecraftApi server) {
        return new PrometheusExporterConfig(server);
    }

    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    MinecraftApi minecraftPluginAdapter(Plugin plugin) {
        return new MinecraftApi(plugin);
    }

    ExporterService exporterService(PrometheusExporterConfig config, MetricService service, MetricsController controller) {
        return new ExporterService(config, service, controller);
    }
}
