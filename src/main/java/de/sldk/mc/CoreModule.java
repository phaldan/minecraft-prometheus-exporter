package de.sldk.mc;

import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;

public class CoreModule {

    public CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    public MetricsController metricsController(Plugin bukkitPlugin, CollectorRegistry registry) {
        return new MetricsController(bukkitPlugin, registry);
    }
}
