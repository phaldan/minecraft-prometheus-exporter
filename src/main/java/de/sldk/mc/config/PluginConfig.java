package de.sldk.mc.config;

import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig<T> {

    protected final String key;

    protected PluginConfig(String key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public T get(FileConfiguration config) {
        return (T) config.get(this.key);
    }
}
