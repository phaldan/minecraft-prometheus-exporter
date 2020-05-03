package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.Gauge;

import java.util.List;

public class TickDurationAverageCollector extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("tick_duration_average"))
            .help("Average duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationAverageCollector(MinecraftApi server) {
        this.server = server;
    }

    private double average(List<Long> ticketDurations) {
        return ticketDurations.stream()
                .mapToDouble(l -> l)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<MetricFamilySamples> collect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(this::average)
                .orElse(-1.0);
        collector.set(value);
        return collector.collect();
    }
}
