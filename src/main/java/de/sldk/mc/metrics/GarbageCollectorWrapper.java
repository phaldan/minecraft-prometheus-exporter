package de.sldk.mc.metrics;

import java.util.List;

import io.prometheus.client.hotspot.GarbageCollectorExports;

public class GarbageCollectorWrapper extends Metric {

    private final GarbageCollectorExports garbageCollectorExports = new GarbageCollectorExports();

    @Override
    public List<MetricFamilySamples> collect() {
        return HotspotPrefixer.prefixFromCollector(garbageCollectorExports);
    }
}