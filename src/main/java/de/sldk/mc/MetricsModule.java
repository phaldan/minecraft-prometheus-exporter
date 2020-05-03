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

public class MetricsModule {

    Map<String, Metric> metrics(MinecraftApi server) {
        Map<String, Metric> metrics = new HashMap<>();
        metrics.put("entities_total", entities(server));
        metrics.put("villagers_total", villagers(server));
        metrics.put("loaded_chunks_total", loadedChunks(server));
        metrics.put("jvm_memory", memory());
        metrics.put("players_online_total", playersOnlineTotal(server));
        metrics.put("players_total", playersTotal(server));
        metrics.put("tps", tps( server));
        metrics.put("jvm_threads", threadsWrapper());
        metrics.put("jvm_gc", garbageCollectorWrapper());
        metrics.put("tick_duration_median", tickDurationMedianCollector(server));
        metrics.put("tick_duration_average", tickDurationAverageCollector(server));
        metrics.put("tick_duration_min", tickDurationMinCollector(server));
        metrics.put("tick_duration_max", tickDurationMaxCollector(server));
        metrics.put("player_online", playerOnline(server));
        metrics.put("player_statistic", playerStatistics(server));
        return metrics;
    }

    private Metric entities(MinecraftApi server) {
        return new Entities(server);
    }

    private Metric villagers(MinecraftApi server) {
        return new Villagers(server);
    }

    private Metric loadedChunks(MinecraftApi server) {
        return new LoadedChunks(server);
    }

    private Metric memory() {
        return new Memory();
    }

    private Metric playersOnlineTotal(MinecraftApi server) {
        return new PlayersOnlineTotal(server);
    }

    private Metric playersTotal(MinecraftApi server) {
        return new PlayersTotal(server);
    }

    private Metric tps(MinecraftApi server) {
        return new Tps(server);
    }

    private Metric threadsWrapper() {
        return new ThreadsWrapper();
    }

    private Metric garbageCollectorWrapper() {
        return new GarbageCollectorWrapper();
    }

    private Metric tickDurationMedianCollector(MinecraftApi server) {
        return new TickDurationMedianCollector(server);
    }

    private Metric tickDurationAverageCollector(MinecraftApi server) {
        return new TickDurationAverageCollector(server);
    }

    private Metric tickDurationMinCollector(MinecraftApi server) {
        return new TickDurationMinCollector(server);
    }

    private Metric tickDurationMaxCollector(MinecraftApi server) {
        return new TickDurationMaxCollector(server);
    }

    private Metric playerOnline(MinecraftApi server) {
        return new PlayerOnline(server);
    }

    private Metric playerStatistics(MinecraftApi server) {
        return new PlayerStatistics(server);
    }
}
