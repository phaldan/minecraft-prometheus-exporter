package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.ThreadExports;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ThreadsWrapper extends Metric {
    public ThreadsWrapper(Plugin plugin, CollectorRegistry registry) {
        super(plugin, new ThreadExportsCollector(), registry);
    }

    private static class ThreadExportsCollector extends Collector {
        private static final ThreadExports threadExports = new ThreadExports();

        @Override
        public List<MetricFamilySamples> collect() {
            return HotspotPrefixer.prefixFromCollector(threadExports);
        }
    }
}