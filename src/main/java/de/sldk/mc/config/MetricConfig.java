package de.sldk.mc.config;

import de.sldk.mc.metrics.Metric;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class MetricConfig extends PluginConfig<Boolean> {

    private static final String CONFIG_PATH_PREFIX = "enable_metrics";

    private final Metric metric;

    protected MetricConfig(String key, Metric metric) {
        super(CONFIG_PATH_PREFIX + "." + key);
        this.metric = metric;
    }

    public Metric getMetric() {
        return metric;
    }
}
