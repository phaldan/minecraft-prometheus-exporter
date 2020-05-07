package de.sldk.mc;

import de.sldk.mc.metrics.Metric;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;

import java.util.Collection;
import java.util.HashSet;

public class MetricRegistry {

    private final Collection<Metric> metrics = new HashSet<>();

    private final Collection<MeterBinder> binders = new HashSet<>();

    private final PrometheusMeterRegistry meterRegistry;

    private final CollectorRegistry collectorRegistry;

    MetricRegistry(PrometheusMeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.collectorRegistry = meterRegistry.getPrometheusRegistry();
    }

    public void register(Metric metric) {
        if (metrics.add(metric)) {
            collectorRegistry.register(metric);
            metric.enable();
        }
    }

    public void register(MeterBinder meterBinder) {
        if (binders.add(meterBinder)) {
            meterBinder.bindTo(meterRegistry);
        }
    }

    public void clear() {
        metrics.forEach(Metric::disable);
        metrics.clear();
        binders.clear();
        collectorRegistry.clear();
        meterRegistry.clear();
    }

    public void collectMetrics() {
    }
}
