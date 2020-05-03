package de.sldk.mc;

import java.util.HashMap;
import java.util.Map;

import de.sldk.mc.config.PrometheusExporterConfig;
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
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

public class MetricsModule {

    PrometheusExporterConfig prometheusExporterConfig(Plugin bukkitPlugin, Map<String, Metric> metrics) {
        return new PrometheusExporterConfig(bukkitPlugin, metrics);
    }

    Map<String, Metric> metrics(Plugin bukkitPlugin, CollectorRegistry registry) {
        Map<String, Metric> metrics = new HashMap<>();
        metrics.put("entities_total", entities(bukkitPlugin, registry));
        metrics.put("villagers_total", villagers(bukkitPlugin, registry));
        metrics.put("loaded_chunks_total", loadedChunks(bukkitPlugin, registry));
        metrics.put("jvm_memory", memory(bukkitPlugin, registry));
        metrics.put("players_online_total", playersOnlineTotal(bukkitPlugin, registry));
        metrics.put("players_total", playersTotal(bukkitPlugin, registry));
        metrics.put("tps", tps(bukkitPlugin, registry));
        metrics.put("jvm_threads", threadsWrapper(bukkitPlugin, registry));
        metrics.put("jvm_gc", garbageCollectorWrapper(bukkitPlugin, registry));
        metrics.put("tick_duration_median", tickDurationMedianCollector(bukkitPlugin, registry));
        metrics.put("tick_duration_average", tickDurationAverageCollector(bukkitPlugin, registry));
        metrics.put("tick_duration_min", tickDurationMinCollector(bukkitPlugin, registry));
        metrics.put("tick_duration_max", tickDurationMaxCollector(bukkitPlugin, registry));
        metrics.put("player_online", playerOnline(bukkitPlugin, registry));
        metrics.put("player_statistic", playerStatistics(bukkitPlugin, registry));
        return metrics;
    }

    private Metric entities(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new Entities(bukkitPlugin, registry);
    }

    private Metric villagers(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new Villagers(bukkitPlugin, registry);
    }

    private Metric loadedChunks(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new LoadedChunks(bukkitPlugin, registry);
    }

    private Metric memory(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new Memory(bukkitPlugin, registry);
    }

    private Metric playersOnlineTotal(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new PlayersOnlineTotal(bukkitPlugin, registry);
    }

    private Metric playersTotal(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new PlayersTotal(bukkitPlugin, registry);
    }

    private Metric tps(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new Tps(bukkitPlugin, registry);
    }

    private Metric threadsWrapper(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new ThreadsWrapper(bukkitPlugin, registry);
    }

    private Metric garbageCollectorWrapper(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new GarbageCollectorWrapper(bukkitPlugin, registry);
    }

    private Metric tickDurationMedianCollector(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new TickDurationMedianCollector(bukkitPlugin, registry);
    }

    private Metric tickDurationAverageCollector(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new TickDurationAverageCollector(bukkitPlugin, registry);
    }

    private Metric tickDurationMinCollector(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new TickDurationMinCollector(bukkitPlugin, registry);
    }

    private Metric tickDurationMaxCollector(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new TickDurationMaxCollector(bukkitPlugin, registry);
    }

    private Metric playerOnline(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new PlayerOnline(bukkitPlugin, registry);
    }

    private Metric playerStatistics(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new PlayerStatistics(bukkitPlugin, registry);
    }
}
