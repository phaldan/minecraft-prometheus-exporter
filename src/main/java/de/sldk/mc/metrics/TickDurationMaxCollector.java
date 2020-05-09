package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import java.util.Collections;

public class TickDurationMaxCollector extends Metric {

    private static final Gauge TD = Gauge.build()
            .name(prefix("tick_duration_max"))
            .help("Max duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMaxCollector(CollectorRegistry registry, MinecraftApi server) {
        super(TD, registry);
        this.server = server;
    }

    @Override
    public void doCollect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(Collections::max)
                .orElse(-1L);
        TD.set(value);
    }
}

