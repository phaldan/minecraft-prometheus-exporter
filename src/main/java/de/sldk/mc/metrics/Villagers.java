package de.sldk.mc.metrics;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftVillager;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import java.util.Map;
import java.util.Objects;

/**
 * Get total count of Villagers.
 * <p>
 * Labelled by
 * <ul>
 *     <li> World ({@link MinecraftWorld#getName()})
 *     <li> Type, e.g. 'desert', 'plains ({@link MinecraftVillager#getVillagerType()})
 *     <li> Profession, e.g. 'fisherman', 'farmer', or 'none' ({@link MinecraftVillager#getProfession()})
 *     <li> Level ({@link MinecraftVillager#getVillagerLevel()})
 * </ul>
 */
public class Villagers extends WorldMetric {

    private static final Gauge VILLAGERS = Gauge.build()
            .name(prefix("villagers_total"))
            .help("Villagers total count, labelled by world, type, profession, and level")
            .labelNames("world", "type", "profession", "level")
            .create();

    public Villagers(CollectorRegistry registry, MinecraftApi server) {
        super(VILLAGERS, registry, server);
    }

    @Override
    public void collect(MinecraftWorld world) {
        Map<VillagerGrouping, Long> mapVillagerGroupingToCount = world.getVillager().stream()
                .collect(groupingBy(VillagerGrouping::new, counting()));

        mapVillagerGroupingToCount.forEach((grouping, count) ->
                VILLAGERS
                        .labels(world.getName(),
                                grouping.type,
                                grouping.profession,
                                Integer.toString(grouping.level))
                        .set(count)
        );
    }

    /**
     * Class used to group villagers together before summation.
     */
    private static class VillagerGrouping {
        private final String type;
        private final String profession;
        private final int level;

        VillagerGrouping(MinecraftVillager villager) {
            this.type = villager.getVillagerType();
            this.profession = villager.getProfession();
            this.level = villager.getVillagerLevel();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VillagerGrouping that = (VillagerGrouping) o;
            return level == that.level &&
                    type.equals(that.type) &&
                    profession.equals(that.profession);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, profession, level);
        }
    }
}
