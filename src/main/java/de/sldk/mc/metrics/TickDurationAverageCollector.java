package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import java.util.List;

public class TickDurationAverageCollector extends Metric {

    private static final Gauge TD = Gauge.build()
            .name(prefix("tick_duration_average"))
            .help("Average duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationAverageCollector(CollectorRegistry registry, MinecraftApi server) {
        super(TD, registry);
        this.server = server;
    }

    private double average(List<Long> ticketDurations) {
        return ticketDurations.stream()
                .mapToDouble(l -> l)
                .average()
                .orElse(0.0);
    }

    @Override
    public void doCollect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(this::average)
                .orElse(-1.0);
        TD.set(value);
    }
}
