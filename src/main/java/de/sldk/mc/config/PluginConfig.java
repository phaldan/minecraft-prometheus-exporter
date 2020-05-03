package de.sldk.mc.config;

import de.sldk.mc.server.MinecraftApi;

public class PluginConfig<T> {

    protected final String key;

    protected PluginConfig(String key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public T get(MinecraftApi server) {
        return (T) server.getConfig(this.key);
    }
}
