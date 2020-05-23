package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class LoadedChunks extends WorldMetric {

    private static final Gauge LOADED_CHUNKS = Gauge.build()
            .name(prefix("loaded_chunks_total"))
            .help("Chunks loaded per world")
            .labelNames("world")
            .create();

    public LoadedChunks(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, LOADED_CHUNKS, registry, server);
    }

    @Override
    public void collect(MinecraftWorld world) {
        LOADED_CHUNKS.labels(world.getName()).set(world.countLoadedChunks());
    }
}
