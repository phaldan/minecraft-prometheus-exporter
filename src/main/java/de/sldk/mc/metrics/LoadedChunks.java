package de.sldk.mc.metrics;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class LoadedChunks extends WorldMetric {

    private static final Gauge LOADED_CHUNKS = Gauge.build()
            .name(prefix("loaded_chunks_total"))
            .help("Chunks loaded per world")
            .labelNames("world")
            .create();

    public LoadedChunks(CollectorRegistry registry, MinecraftApi server) {
        super(LOADED_CHUNKS, registry, server);
    }

    @Override
    public void collect(MinecraftWorld world) {
        LOADED_CHUNKS.labels(world.getName()).set(world.countLoadedChunks());
    }
}
