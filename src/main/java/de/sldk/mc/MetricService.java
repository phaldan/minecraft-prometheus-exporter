package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;

public class MetricService {

    private final PrometheusExporterConfig config;
    private final MetricRegistry registry;

    public MetricService(PrometheusExporterConfig config, MetricRegistry registry) {
        this.config = config;
        this.registry = registry;
    }

    public void enableMetrics() {
        config.getEnabledMetrics().forEach(m -> {
            registry.register(m);
            m.enable();
        });
    }

    public void disableMetrics() {
        config.getEnabledMetrics().forEach(m -> {
            registry.unregister(m);
            m.disable();
        });
    }
}
