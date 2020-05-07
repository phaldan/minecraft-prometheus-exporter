package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class PrometheusExporter extends JavaPlugin {

    private final CoreModule coreModule = new CoreModule();

    private final MetricsModule metricModule = new MetricsModule();

    private final CollectorRegistry registry = coreModule.collectorRegistry();

    private final MetricRegistry metricRegistry = coreModule.metricRegistry();

    private final MinecraftApi minecraftServer = coreModule.minecraftPluginAdapter(this);

    private final MetricsController controller = coreModule.metricsController(minecraftServer, registry, metricRegistry);

    private final PrometheusExporterConfig config = coreModule.prometheusExporterConfig(minecraftServer);

    private final Map<String, Metric> metrics = metricModule.metrics(minecraftServer);

    private final MetricService service = coreModule.metricService(config, metricRegistry, registry, metrics);

    @Override
    public void onEnable() {
        config.loadDefaultsAndSave();

        service.enableMetrics();

        serveMetrics();
    }

    private void serveMetrics() {
        int port = config.get(PrometheusExporterConfig.PORT);
        String host = config.get(PrometheusExporterConfig.HOST);
        controller.startServer(host, port);
    }

    @Override
    public void onDisable() {
        controller.stopServer();
        service.disableMetrics();
    }

}
