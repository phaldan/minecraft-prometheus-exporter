package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.util.Collections;

public class TickDurationMinCollector extends Metric {

    private static final Gauge TD = Gauge.build()
            .name(prefix("tick_duration_min"))
            .help("Min duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMinCollector(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, TD, registry);
        this.server = server;
    }

    @Override
    public void doCollect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(Collections::min)
                .orElse(-1L);
        TD.set(value);
    }
}

