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
        config.getEnabledMetrics().forEach(m -> {
            registry.register(m);
            collectorRegistry.register(m.getCollector());
            m.enable();
        });
    }

    public void disableMetrics() {
        config.getEnabledMetrics().forEach(m -> {
            registry.unregister(m);
            collectorRegistry.unregister(m.getCollector());
            m.disable();
        });
    }
}
