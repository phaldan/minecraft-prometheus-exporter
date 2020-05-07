package de.sldk.mc;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.bukkit.plugin.Plugin;
import org.eclipse.jetty.server.Server;

import java.util.List;
import java.util.Map;

public class CoreModule {

    MetricsController metricsController(MinecraftApi minecraftServer, MetricRegistry metricRegistry) {
        return new MetricsController(minecraftServer, metricRegistry, new Server());
    }

    MetricService metricService(
            PrometheusExporterConfig config,
            MetricRegistry metricRegistry,
            Map<String, Metric> metrics,
            Map<String, List<MeterBinder>> meterBinders) {
        return new MetricService(config, metricRegistry, metrics, meterBinders);
    }

    PrometheusExporterConfig prometheusExporterConfig(MinecraftApi server) {
        return new PrometheusExporterConfig(server);
    }

    MetricRegistry metricRegistry(PrometheusMeterRegistry meterRegistry) {
        return new MetricRegistry(meterRegistry);
    }

    MinecraftApi minecraftPluginAdapter(Plugin plugin) {
        return new MinecraftApi(plugin);
    }

    ExporterService exporterService(PrometheusExporterConfig config, MetricService service, MetricsController controller) {
        return new ExporterService(config, service, controller);
    }
}
