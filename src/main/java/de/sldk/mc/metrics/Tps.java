package de.sldk.mc.metrics;

import java.util.List;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.tps.TpsCollector;
import io.prometheus.client.Gauge;

public class Tps extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .create();

    private final TpsCollector tpsCollector = new TpsCollector();

    private final MinecraftApi server;

    private int taskId;

    public Tps(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        collector.set(tpsCollector.getAverageTPS());
        return collector.collect();
    }

    @Override
    public void enable() {
        this.taskId = server.scheduleSyncRepeatingTask(tpsCollector, 0, TpsCollector.POLL_INTERVAL);
    }

    @Override
    public void disable() {
        server.cancelScheduledTask(taskId);
    }
}
