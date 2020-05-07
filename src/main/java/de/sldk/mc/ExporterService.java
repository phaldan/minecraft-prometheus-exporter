package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;

public class ExporterService {

    private final PrometheusExporterConfig config;
    private final MetricService service;
    private final MetricsController controller;

    ExporterService(PrometheusExporterConfig config, MetricService service, MetricsController controller) {
        this.config = config;
        this.service = service;
        this.controller = controller;
    }

    public void setup() {
        config.loadDefaultsAndSave();
        service.enableMetrics();
        serveMetrics();
    }

    private void serveMetrics() {
        int port = config.get(PrometheusExporterConfig.PORT);
        String host = config.get(PrometheusExporterConfig.HOST);
        controller.startServer(host, port);
    }

    public void teardown() {
        controller.stopServer();
        service.disableMetrics();
    }
}
