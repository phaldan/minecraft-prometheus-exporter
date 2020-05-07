package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.server.MinecraftApi;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
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
        MicrometerModule micrometerModule = new MicrometerModule();
        CollectorRegistry registry = coreModule.collectorRegistry();
        PrometheusMeterRegistry meterRegistry = micrometerModule.prometheusMeterRegistry(registry);
        MetricRegistry metricRegistry = coreModule.metricRegistry(meterRegistry);
        MinecraftApi minecraftServer = coreModule.minecraftPluginAdapter(plugin);
        MetricsController controller = coreModule.metricsController(minecraftServer, registry, metricRegistry);
        PrometheusExporterConfig config = coreModule.prometheusExporterConfig(minecraftServer);
        Map<String, Metric> metrics = metricModule.metrics(minecraftServer);
        Map<String, List<MeterBinder>> meterBinders = micrometerModule.meterBinders();
        MetricService service = coreModule.metricService(config, metricRegistry, metrics, meterBinders);
        return coreModule.exporterService(config, service, controller);
    }
}
