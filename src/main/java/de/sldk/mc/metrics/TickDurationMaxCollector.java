package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.Collections;
import java.util.List;

public class TickDurationMaxCollector extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("tick_duration_max"))
            .help("Max duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMaxCollector(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(Collections::max)
                .orElse(-1L);
        collector.set(value);
        return collector.collect();
    }
}

