package de.sldk.mc;

import de.sldk.mc.logging.Logger;
import de.sldk.mc.logging.LoggerFactory;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MetricsController extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger();
    private final MetricRegistry metricRegistry;
    private final MinecraftApi minecraftServer;
    private final CollectorRegistry registry;
    private final Server server;

    public MetricsController(MinecraftApi minecraftServer, CollectorRegistry registry, MetricRegistry metricRegistry, Server server) {
        this.minecraftServer = minecraftServer;
        this.registry = registry;
        this.metricRegistry = metricRegistry;
        this.server = server;
    }

    public void startServer(String host, int port) {
        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(this);

        server.setConnectors(createConnector(host, port));
        server.setHandler(gzipHandler);

        try {
            server.start();
            logger.info("Started Prometheus metrics endpoint at: {0}:{1}", host, port);
        } catch (Exception e) {
            logger.severe("Could not start embedded Jetty server", e);
        }
    }

    private Connector[] createConnector(String host, int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(host);
        connector.setPort(port);
        return new Connector[]{connector};
    }

    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.warn("Failed to stop metrics server gracefully", e);
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!target.equals("/metrics")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /*
         * Bukkit API calls have to be made from the main thread.
         * That's why we use the BukkitScheduler to retrieve the server stats.
         * */
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(TextFormat.CONTENT_TYPE_004);
            minecraftServer.callSyncMethod(() -> {
                metricRegistry.collectMetrics();
                TextFormat.write004(response.getWriter(), registry.metricFamilySamples());
                return null;
            }).get();

            baseRequest.setHandled(true);
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Failed to read server statistic: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
