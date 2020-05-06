package de.sldk.mc.config;

import de.sldk.mc.server.MinecraftApi;

public class PrometheusExporterConfig {

    private static final String METRIC_PATH_PREFIX = "enable_metrics.";

    public static final PluginConfig<String> HOST = new PluginConfig<>("host");

    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port");

    private final MinecraftApi server;

    public PrometheusExporterConfig(MinecraftApi server) {
        this.server = server;
    }

    public void loadDefaultsAndSave() {
        server.loadDefaultConfig();
    }

    public boolean isMetricEnabled(String name) {
        String fullPath = METRIC_PATH_PREFIX + name;
        return server.getConfigAsBoolean(fullPath);
    }

    public <T> T get(PluginConfig<T> config) {
        return config.get(server);
    }
}
