package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static java.util.Objects.nonNull;

public class PrometheusExporter extends JavaPlugin {

    private ExporterService service;

    @Override
    public void onLoad() {
        service = createServiceInstance(this);
    }

    @Override
    public void onEnable() {
        if (nonNull(service)) {
            service.setup();
        }
    }

    @Override
    public void onDisable() {
        if (nonNull(service)) {
            service.teardown();
        }
    }

    static ExporterService createServiceInstance(Plugin plugin) {
        CoreModule coreModule = new CoreModule();
        MetricsModule metricModule = new MetricsModule();
        CollectorRegistry registry = coreModule.collectorRegistry();
        MetricRegistry metricRegistry = coreModule.metricRegistry();
        MinecraftApi minecraftServer = coreModule.minecraftPluginAdapter(plugin);
        MetricsController controller = coreModule.metricsController(minecraftServer, registry, metricRegistry);
        PrometheusExporterConfig config = coreModule.prometheusExporterConfig(minecraftServer);
        Map<String, Metric> metrics = metricModule.metrics(minecraftServer);
        MetricService service = coreModule.metricService(config, metricRegistry, registry, metrics);
        return coreModule.exporterService(config, service, controller);
    }
}
