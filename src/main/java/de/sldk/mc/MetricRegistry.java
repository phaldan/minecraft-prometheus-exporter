package de.sldk.mc;

import de.sldk.mc.logging.Logger;
import de.sldk.mc.logging.LoggerFactory;
import de.sldk.mc.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

public class MetricRegistry {

    private final Logger logger = LoggerFactory.getLogger();
    private final List<Metric> metrics = new ArrayList<>();

    MetricRegistry() {
    }

    public void register(Metric metric) {
        metrics.add(metric);
    }

    public void unregister(Metric metric) {
        metrics.remove(metric);
    }

    void collectMetrics() {
        this.metrics.forEach(this::collectWithErrorHandling);
    }

    private void collectWithErrorHandling(Metric metric) {
        try {
            metric.doCollect();
        } catch (Exception e) {
            String metricName = metric.getClass().getName();
            logger.warn("Failed to collect metric for {0}", metricName, e);
        }
    }
}
