package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.tps.TpsCollector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class Tps extends Metric {

    private static final Gauge TPS = Gauge.build()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .create();

    private final TpsCollector tpsCollector = new TpsCollector();

    private final MinecraftApi server;

    private int taskId;

    public Tps(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, TPS, registry);
        this.server = server;
    }

    @Override
    public void enable() {
        super.enable();
        this.taskId = server.scheduleSyncRepeatingTask(tpsCollector, 0, TpsCollector.POLL_INTERVAL);
    }

    @Override
    public void disable() {
        super.disable();
        server.cancelScheduledTask(taskId);
    }

    @Override
    public void doCollect() {
        TPS.set(tpsCollector.getAverageTPS());
    }
}
