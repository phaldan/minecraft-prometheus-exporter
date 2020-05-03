package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.Collections;
import java.util.List;

public class TickDurationMedianCollector extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("tick_duration_median"))
            .help("Median duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMedianCollector(MinecraftApi server) {
        this.server = server;
    }

    private double median(List<Long> times) {
        /* Copy the original array - don't want to sort it! */
        Collections.sort(times);
        int middle = times.size() / 2;
        return times.size() % 2 == 1 ? times.get(middle) : (times.get(middle - 1) + times.get(middle)) / 2.0;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(this::median)
                .orElse(-1.0);
        collector.set(value);
        return collector.collect();
    }
}
