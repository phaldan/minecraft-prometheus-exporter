package de.sldk.mc.metrics;

import io.prometheus.client.hotspot.ThreadExports;

import java.util.List;

public class ThreadsWrapper extends Metric {

    private final ThreadExports threadExports = new ThreadExports();

    @Override
    public List<MetricFamilySamples> collect() {
        return HotspotPrefixer.prefixFromCollector(threadExports);
    }
}