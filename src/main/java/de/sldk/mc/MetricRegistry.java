package de.sldk.mc;

import de.sldk.mc.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

public class MetricRegistry {

    private final List<Metric> metrics = new ArrayList<>();

    MetricRegistry() {
    }

    public void register(Metric metric) {
        metrics.add(metric);
    }

    public void unregister(Metric metric) {
        metrics.remove(metric);
    }
}
