package de.sldk.mc.metrics;

import java.util.List;

import io.prometheus.client.CollectorRegistry;

import io.prometheus.client.Collector;
import io.prometheus.client.hotspot.GarbageCollectorExports;

public class GarbageCollectorWrapper extends Metric {
    public GarbageCollectorWrapper(CollectorRegistry registry) {
        super(new GarbageCollectorExportsCollector(), registry);
    }

    private static class GarbageCollectorExportsCollector extends Collector {
        private static final GarbageCollectorExports garbageCollectorExports = new GarbageCollectorExports();

        @Override
        public List<MetricFamilySamples> collect() {
            return HotspotPrefixer.prefixFromCollector(garbageCollectorExports);
        }
    }
}