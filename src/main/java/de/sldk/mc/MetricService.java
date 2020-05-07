package de.sldk.mc;

import de.sldk.mc.config.PrometheusExporterConfig;
import de.sldk.mc.logging.Logger;
import de.sldk.mc.logging.LoggerFactory;
import de.sldk.mc.metrics.Metric;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MetricService {

    private final Logger logger = LoggerFactory.getLogger();

    private final PrometheusExporterConfig config;
    private final MetricRegistry registry;
    private final Map<String, Metric> metrics;
    private final Map<String, List<MeterBinder>> meterBinders;

    MetricService(PrometheusExporterConfig config,
                         MetricRegistry registry,
                         Map<String, Metric> metrics,
                         Map<String, List<MeterBinder>> meterBinders) {
        this.config = config;
        this.registry = registry;
        this.metrics = metrics;
        this.meterBinders = meterBinders;
    }

    public void enableMetrics() {
        getEnabledMetric(metrics).forEach(registry::register);
        getEnabledMeterBinder(meterBinders).flatMap(Collection::stream).forEach(registry::register);
    }

    public void disableMetrics() {
        registry.clear();
    }

    private Stream<Metric> getEnabledMetric(Map<String, Metric> map) {
        return map.entrySet().stream()
                .filter(entry -> config.isMetricEnabled(entry.getKey()))
                .map(this::getEnabledMetric);
    }

    private Metric getEnabledMetric(Map.Entry<String, Metric> entry) {
        Metric metric = entry.getValue();
        logger.info("Enable {0} via config `{1}` (see enable_metrics)", metric.getClass().getSimpleName(), entry.getKey());
        return metric;
    }

    private Stream<List<MeterBinder>> getEnabledMeterBinder(Map<String, List<MeterBinder>> map) {
        return map.entrySet().stream()
                .filter(entry -> config.isMetricEnabled(entry.getKey()))
                .map(this::getEnabledMeterBinder);
    }

    private List<MeterBinder> getEnabledMeterBinder(Map.Entry<String, List<MeterBinder>> entry) {
        List<MeterBinder> list = entry.getValue();
        list.forEach(mb -> logger.info("Enable {0} via config `{1}` (see enable_metrics)", mb.getClass().getSimpleName(), entry.getKey()));
        return list;
    }
}
