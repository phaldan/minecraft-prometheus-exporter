package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;

import java.util.List;

public class Memory extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("jvm_memory"))
            .help("JVM memory usage")
            .labelNames("type")
            .create();

    @Override
    public List<MetricFamilySamples> collect() {
        doCollect();
        return collector.collect();
    }

    public void doCollect() {
        collector.labels("max").set(Runtime.getRuntime().maxMemory());
        collector.labels("free").set(Runtime.getRuntime().freeMemory());
        collector.labels("allocated").set(Runtime.getRuntime().totalMemory());
    }
}
