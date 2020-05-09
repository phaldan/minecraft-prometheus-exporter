package de.sldk.mc.metrics;

import static java.util.stream.Collectors.toMap;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftPlayerStatistic;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerStatistics extends PlayerMetric {

    private static final Gauge PLAYER_STATS = Gauge.build()
            .name(prefix("player_statistic"))
            .help("Player statistics")
            .labelNames("player_name", "player_uid", "statistic")
            .create();

    private final List<MinecraftPlayerStatistic> statistic;
    private final List<MinecraftEntityType> entityTypes;
    private final List<String> materials;

    public PlayerStatistics(CollectorRegistry registry, MinecraftApi server) {
        super(PLAYER_STATS, registry, server);
        statistic = server.getStatistics();
        entityTypes = server.getEntityTypes();
        materials = server.getMaterials();
    }

    @Override
    public void collect(MinecraftPlayer player) {
        getStatistics(player).forEach((stat, value) -> PLAYER_STATS.labels(player.getName(), player.getUniqueId(), stat).set(value));
    }

    private Map<String, Integer> getStatistics(MinecraftPlayer player) {
        return Optional.of(player)
                .map(this::getPlayerStatistic)
                .orElseGet(Collections::emptyMap);
    }

    private Map<String, Integer> getPlayerStatistic(MinecraftPlayer player) {
        return statistic.stream()
                .collect(toMap(MinecraftPlayerStatistic::getName, s -> getStatistic(player, s)));
    }

    private Integer getStatistic(MinecraftPlayer player, MinecraftPlayerStatistic statistic) {
        if (statistic.isUntyped()) {
            return player.getStatistic(statistic);
        } else if (statistic.isEntity()) {
            return entityTypes.stream()
                    .map(type -> player.getStatistic(statistic, type))
                    .reduce(0,  Integer::sum);
        } else if (statistic.isMaterial()) {
            return materials.stream()
                    .map(material -> player.getStatistic(statistic, material))
                    .reduce(0,  Integer::sum);
        }
        return 0;
    }
}
