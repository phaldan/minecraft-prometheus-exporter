package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Level;

public class PrometheusExporter extends JavaPlugin {

    private final CoreModule coreModule = new CoreModule();

    private final MetricsModule metricModule = new MetricsModule();

    private final CollectorRegistry registry = coreModule.collectorRegistry();

    private final MetricRegistry metricRegistry = coreModule.metricRegistry();

    private final MetricsController controller = coreModule.metricsController(this, registry, metricRegistry);

    private final MinecraftApi minecraftServer = coreModule.minecraftPluginAdapter(this);

    private final PrometheusExporterConfig config = coreModule.prometheusExporterConfig(minecraftServer);

    private final Map<String, Metric> metrics = metricModule.metrics(minecraftServer);

    private final MetricService service = coreModule.metricService(config, metricRegistry, registry, metrics);

    private Server server;

    @Override
    public void onEnable() {
        config.loadDefaultsAndSave();

        service.enableMetrics();

        serveMetrics();
    }

    private void serveMetrics() {
        int port = config.get(PrometheusExporterConfig.PORT);
        String host = config.get(PrometheusExporterConfig.HOST);

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(controller);

        InetSocketAddress address = new InetSocketAddress(host, port);
        server = new Server(address);
        server.setHandler(gzipHandler);

        try {
            server.start();
            getLogger().info("Started Prometheus metrics endpoint at: " + host + ":" + port);

        } catch (Exception e) {
            getLogger().severe("Could not start embedded Jetty server");
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Failed to stop metrics server gracefully: " + e.getMessage());
                getLogger().log(Level.FINE, "Failed to stop metrics server gracefully", e);
            }
        }
    }

}
