package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import io.prometheus.client.CollectorRegistry;

public class MetricService {

    private final PrometheusExporterConfig config;
    private final MetricRegistry registry;
    private final CollectorRegistry collectorRegistry;

    public MetricService(PrometheusExporterConfig config, MetricRegistry registry, CollectorRegistry collectorRegistry) {
        this.config = config;
        this.registry = registry;
        this.collectorRegistry = collectorRegistry;
    }

    public void enableMetrics() {
        config.getEnabledMetrics().forEach(metric -> {
            registry.register(metric);
            collectorRegistry.register(metric);
            metric.enable();
        });
    }

    public void disableMetrics() {
        config.getEnabledMetrics().forEach(metric -> {
            registry.unregister(metric);
            collectorRegistry.unregister(metric);
            metric.disable();
        });
    }
}
