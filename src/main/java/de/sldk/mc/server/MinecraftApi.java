package de.sldk.mc.server;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class MinecraftApi {

    private static final long[] TICK_DURATIONS_PLACEHOLDER = new long[] { -1 };

    private final Plugin plugin;

    private final Server bukkitServer;

    private final BukkitScheduler scheduler;

    /*
     * If reflection is successful, this will hold a reference directly to the
     * MinecraftServer internal tick duration tracker
     */
    private long[] tickDurationReference = null;

    public MinecraftApi(Plugin plugin) {
        this.plugin = plugin;
        this.bukkitServer = plugin.getServer();
        this.scheduler = bukkitServer.getScheduler();
    }

    public int scheduleSyncRepeatingTask(Runnable task, long delay, long period) {
        return scheduler.scheduleSyncRepeatingTask(plugin, task, delay, period);
    }

    public void cancelScheduledTask(int taskId) {
        scheduler.cancelTask(taskId);
    }

    public List<MinecraftWorld> getWorlds() {
        return bukkitServer.getWorlds().stream()
                .map(MinecraftWorld::new)
                .collect(toList());
    }

    public int countPlayers() {
        return bukkitServer.getOfflinePlayers().length;
    }

    public List<MinecraftPlayer> getPlayers() {
        return Arrays.stream(bukkitServer.getOfflinePlayers())
                .map(MinecraftPlayer::new)
                .collect(toList());
    }

    public List<MinecraftPlayerStatistic> getStatistics() {
        return Arrays.stream(Statistic.values())
                .map(MinecraftPlayerStatistic::new)
                .collect(toList());
    }

    public List<MinecraftEntityType> getEntityTypes() {
        return Arrays.stream(EntityType.values())
                .map(MinecraftEntityType::new)
                .collect(toList());
    }

    public List<String> getMaterials() {
        return Arrays.stream(Material.values())
                .map(Material::name)
                .collect(toList());
    }

    public Optional<List<Long>> getTickDurations() {
        if (tickDurationReference == null) {
            tickDurationReference = tryToRetrieveTickDurationsReference().orElseGet(() -> {
                plugin.getLogger().log(Level.WARNING, "Failed to find tick times buffer via reflection. Tick duration metrics will not be available.");
                return TICK_DURATIONS_PLACEHOLDER;
            });
        }
        return Optional.of(tickDurationReference)
                .filter(d -> tickDurationReference != TICK_DURATIONS_PLACEHOLDER)
                .map(durations -> Arrays.stream(tickDurationReference).boxed().collect(toList()));
    }

    /**
     * If there is not yet a handle to the internal tick duration buffer, try
     * to acquire one using reflection.
     *
     * This searches for any long[] array in the MinecraftServer class. It should
     * work across many versions of Spigot/Paper and various obfuscation mappings
     */
    private Optional<long[]> tryToRetrieveTickDurationsReference() {
        long[] longestArray = null;

        try {
            Method getServerMethod = bukkitServer.getClass().getMethod("getServer");
            Object minecraftServer = getServerMethod.invoke(bukkitServer);

            /* Look for the only array of longs in that class, which is tick duration */
            for (Field field : minecraftServer.getClass().getSuperclass().getDeclaredFields()) {
                if (field.getType().isArray() && field.getType().getComponentType().equals(long.class)) {
                    /* Check all the long[] items in this class, and remember the one with the most elements */
                    long[] array = (long[]) field.get(minecraftServer);
                    if (array != null && (longestArray == null || array.length > longestArray.length)) {
                        longestArray = array;
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.FINE, "Caught exception looking for tick times array: ", e);
        }
        return ofNullable(longestArray);
    }
}
