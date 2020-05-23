package de.sldk.mc;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

public class CoreModule {

    CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    MetricsController metricsController(Plugin bukkitPlugin, CollectorRegistry registry, MetricRegistry metricRegistry) {
        return new MetricsController(bukkitPlugin, registry, metricRegistry);
    }

    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    MinecraftApi minecraftPluginAdapter(Plugin plugin) {
        return new MinecraftApi(plugin);
    }
}
