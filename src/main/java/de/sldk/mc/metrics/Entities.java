package de.sldk.mc.metrics;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntity;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftWorld;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * Get current count of all entities.
 *
 * Entities are labelled by
 * <ol>
 *     <li> world,
 *     <li> type ({@link MinecraftEntityType}),
 *     <li> alive ({@link MinecraftEntityType#isAlive()}),
 *     <li> and spawnable ({@link MinecraftEntityType#isSpawnable()})
 * </ol>
 */
public class Entities extends WorldMetric {

    private static final Gauge ENTITIES = Gauge.build()
            .name(prefix("entities_total"))
            .help("Entities loaded per world")
            .labelNames("world", "type", "alive", "spawnable")
            .create();

    /**
     * Override the value returned by {@link MinecraftEntityType#isAlive()}.
     */
    private static final Map<String, Boolean> ALIVE_OVERRIDE = singletonMap("armor_stand", false);

    public Entities(Plugin plugin, CollectorRegistry registry, MinecraftApi server) {
        super(plugin, ENTITIES, registry, server);
        ENTITIES.clear();
    }

    @Override
    public void collect(MinecraftWorld world) {
        Map<MinecraftEntityType, Long> mapEntityTypesToCounts = world.getEntities().stream()
                .collect(groupingBy(MinecraftEntity::getType, counting()));

        mapEntityTypesToCounts
                .forEach((entityType, count) ->
                        ENTITIES
                                .labels(world.getName(),
                                        entityType.getName(),
                                        Boolean.toString(isEntityTypeAlive(entityType)),
                                        Boolean.toString(entityType.isSpawnable()))
                                .set(count)
                );
    }

    private boolean isEntityTypeAlive(MinecraftEntityType type) {
        return ALIVE_OVERRIDE.getOrDefault(type.getName(), type.isAlive());
    }
}
