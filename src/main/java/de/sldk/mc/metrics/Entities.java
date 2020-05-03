package de.sldk.mc.metrics;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntity;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftWorld;
import io.prometheus.client.Gauge;

import java.util.List;
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
public class Entities extends Metric {

    /**
     * Override the value returned by {@link MinecraftEntityType#isAlive()}.
     */
    private static final Map<String, Boolean> ALIVE_OVERRIDE = singletonMap("armor_stand", false);

    private final Gauge collector = Gauge.build()
            .name(prefix("entities_total"))
            .help("Entities loaded per world")
            .labelNames("world", "type", "alive", "spawnable")
            .create();

    private final MinecraftApi server;

    public Entities(MinecraftApi server) {
        this.server = server;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        server.getWorlds().forEach(this::collect);
        return collector.collect();
    }

    private void collect(MinecraftWorld world) {
        world.getEntities().stream()
                .collect(groupingBy(MinecraftEntity::getType, counting()))
                .forEach((entityType, count) ->
                        collector.labels(world.getName(),
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
