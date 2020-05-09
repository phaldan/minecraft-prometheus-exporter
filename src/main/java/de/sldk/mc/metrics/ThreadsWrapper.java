package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.ThreadExports;

import java.util.List;

public class ThreadsWrapper extends Metric {
    public ThreadsWrapper(CollectorRegistry registry) {
        super(new ThreadExportsCollector(), registry);
    }

    private static class ThreadExportsCollector extends Collector {
        private static final ThreadExports threadExports = new ThreadExports();

        @Override
        public List<MetricFamilySamples> collect() {
            return HotspotPrefixer.prefixFromCollector(threadExports);
        }
    }
}