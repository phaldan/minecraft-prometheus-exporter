package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.logging.Logger;
import de.sldk.mc.logging.LoggerFactory;
import de.sldk.mc.metrics.Metric;
import io.prometheus.client.CollectorRegistry;

import java.util.Map;
import java.util.stream.Stream;

public class MetricService {

    private final Logger logger = LoggerFactory.getLogger();

    private final PrometheusExporterConfig config;
    private final MetricRegistry registry;
    private final CollectorRegistry collectorRegistry;
    private final Map<String, Metric> metrics;

    public MetricService(PrometheusExporterConfig config,
                         MetricRegistry registry,
                         CollectorRegistry collectorRegistry,
                         Map<String, Metric> metrics) {
        this.config = config;
        this.registry = registry;
        this.collectorRegistry = collectorRegistry;
        this.metrics = metrics;
    }

    public void enableMetrics() {
        getEnabledMetrics().forEach(metric -> {
            registry.register(metric);
            collectorRegistry.register(metric);
            metric.enable();
        });

    }

    public void disableMetrics() {
        getEnabledMetrics().forEach(metric -> {
            registry.unregister(metric);
            collectorRegistry.unregister(metric);
            metric.disable();
        });
    }

    private Stream<Metric> getEnabledMetrics() {
        return metrics.entrySet().stream()
                .filter(entry -> config.isMetricEnabled(entry.getKey()))
                .map(this::getEnabledMetric);
    }

    private Metric getEnabledMetric(Map.Entry<String, Metric> entry) {
        Metric metric = entry.getValue();
        logger.info("Enable metric {0} ({1})", metric.getClass().getSimpleName(), entry.getKey());
        return metric;
    }
}
