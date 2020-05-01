package de.sldk.mc.metrics;

import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

import io.prometheus.client.Gauge;

public class TickDurationMaxCollector extends TickDurationCollector {
    private static final String NAME = "tick_duration_max";

    private static final Gauge TD = Gauge.build()
            .name(prefix(NAME))
            .help("Max duration of server tick (nanoseconds)")
            .create();

    public TickDurationMaxCollector(Plugin plugin, CollectorRegistry registry) {
        super(plugin, TD, registry);
    }

    private long getTickDurationMax() {
        long max = Long.MIN_VALUE;
        for (Long val : getTickDurations()) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    @Override
    public void doCollect() {
        TD.set(getTickDurationMax());
    }
}

