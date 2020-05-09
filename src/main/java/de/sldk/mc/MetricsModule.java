package de.sldk.mc;

import java.util.HashMap;
import java.util.Map;

import de.sldk.mc.metrics.Entities;
import de.sldk.mc.metrics.GarbageCollectorWrapper;
import de.sldk.mc.metrics.LoadedChunks;
import de.sldk.mc.metrics.Memory;
import de.sldk.mc.metrics.Metric;
import de.sldk.mc.metrics.PlayerOnline;
import de.sldk.mc.metrics.PlayerStatistics;
import de.sldk.mc.metrics.PlayersOnlineTotal;
import de.sldk.mc.metrics.PlayersTotal;
import de.sldk.mc.metrics.ThreadsWrapper;
import de.sldk.mc.metrics.TickDurationAverageCollector;
import de.sldk.mc.metrics.TickDurationMaxCollector;
import de.sldk.mc.metrics.TickDurationMedianCollector;
import de.sldk.mc.metrics.TickDurationMinCollector;
import de.sldk.mc.metrics.Tps;
import de.sldk.mc.metrics.Villagers;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;

public class MetricsModule {

    Map<String, Metric> metrics(CollectorRegistry registry, MinecraftApi server) {
        Map<String, Metric> metrics = new HashMap<>();
        metrics.put("entities_total", entities(registry, server));
        metrics.put("villagers_total", villagers(registry, server));
        metrics.put("loaded_chunks_total", loadedChunks(registry, server));
        metrics.put("jvm_memory", memory(registry));
        metrics.put("players_online_total", playersOnlineTotal(registry, server));
        metrics.put("players_total", playersTotal(registry, server));
        metrics.put("tps", tps(registry, server));
        metrics.put("jvm_threads", threadsWrapper(registry));
        metrics.put("jvm_gc", garbageCollectorWrapper(registry));
        metrics.put("tick_duration_median", tickDurationMedianCollector(registry, server));
        metrics.put("tick_duration_average", tickDurationAverageCollector(registry, server));
        metrics.put("tick_duration_min", tickDurationMinCollector(registry, server));
        metrics.put("tick_duration_max", tickDurationMaxCollector(registry, server));
        metrics.put("player_online", playerOnline(registry, server));
        metrics.put("player_statistic", playerStatistics(registry, server));
        return metrics;
    }

    private Metric entities(CollectorRegistry registry, MinecraftApi server) {
        return new Entities(registry, server);
    }

    private Metric villagers(CollectorRegistry registry, MinecraftApi server) {
        return new Villagers(registry, server);
    }

    private Metric loadedChunks(CollectorRegistry registry, MinecraftApi server) {
        return new LoadedChunks(registry, server);
    }

    private Metric memory(CollectorRegistry registry) {
        return new Memory(registry);
    }

    private Metric playersOnlineTotal(CollectorRegistry registry, MinecraftApi server) {
        return new PlayersOnlineTotal(registry, server);
    }

    private Metric playersTotal(CollectorRegistry registry, MinecraftApi server) {
        return new PlayersTotal(registry, server);
    }

    private Metric tps(CollectorRegistry registry, MinecraftApi server) {
        return new Tps(registry, server);
    }

    private Metric threadsWrapper(CollectorRegistry registry) {
        return new ThreadsWrapper(registry);
    }

    private Metric garbageCollectorWrapper(CollectorRegistry registry) {
        return new GarbageCollectorWrapper(registry);
    }

    private Metric tickDurationMedianCollector(CollectorRegistry registry, MinecraftApi server) {
        return new TickDurationMedianCollector(registry, server);
    }

    private Metric tickDurationAverageCollector(CollectorRegistry registry, MinecraftApi server) {
        return new TickDurationAverageCollector(registry, server);
    }

    private Metric tickDurationMinCollector(CollectorRegistry registry, MinecraftApi server) {
        return new TickDurationMinCollector(registry, server);
    }

    private Metric tickDurationMaxCollector(CollectorRegistry registry, MinecraftApi server) {
        return new TickDurationMaxCollector(registry, server);
    }

    private Metric playerOnline(CollectorRegistry registry, MinecraftApi server) {
        return new PlayerOnline(registry, server);
    }

    private Metric playerStatistics(CollectorRegistry registry, MinecraftApi server) {
        return new PlayerStatistics(registry, server);
    }
}
