package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class TickDurationMedianCollector extends Metric {

    private static final Gauge TD = Gauge.build()
            .name(prefix("tick_duration_median"))
            .help("Median duration of server tick (nanoseconds)")
            .create();

    private final MinecraftApi server;

    public TickDurationMedianCollector(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, TD, registry);
        this.server = server;
    }

    private double median(List<Long> times) {
        /* Copy the original array - don't want to sort it! */
        Collections.sort(times);
        int middle = times.size() / 2;
        return times.size() % 2 == 1 ? times.get(middle) : (times.get(middle - 1) + times.get(middle)) / 2.0;
    }

    @Override
    public void doCollect() {
        double value = server.getTickDurations()
                .filter(list -> !list.isEmpty())
                .map(this::median)
                .orElse(-1.0);
        TD.set(value);
    }
}
