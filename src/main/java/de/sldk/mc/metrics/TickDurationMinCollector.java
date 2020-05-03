package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.Collections;
import java.util.List;

public class TickDurationMinCollector extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("tick_duration_min"))
            .help("Min duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMinCollector(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(Collections::min)
                .orElse(-1L);
        collector.set(value);
        return collector.collect();
    }
}
