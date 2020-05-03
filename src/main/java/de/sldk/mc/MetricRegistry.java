package de.sldk.mc;

import de.sldk.mc.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

public class MetricRegistry {

    private final List<Metric> metrics = new ArrayList<>();

    public MetricRegistry() {
        
    }
    
    public void register(Metric metric) {
        this.metrics.add(metric);
    }

    void collectMetrics() {
        this.metrics.forEach(Metric::collect);
    }

}
