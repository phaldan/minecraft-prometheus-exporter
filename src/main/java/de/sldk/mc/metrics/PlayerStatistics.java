package de.sldk.mc.metrics;

import static java.util.stream.Collectors.toMap;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftPlayerStatistic;
import io.prometheus.client.Gauge;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerStatistics extends Metric {

    private final Gauge collector = Gauge.build()
            .name(prefix("player_statistic"))
            .help("Player statistics")
            .labelNames("player_name", "player_uid", "statistic")
            .create();

    private final MinecraftApi server;
    private final List<MinecraftPlayerStatistic> statistic;
    private final List<MinecraftEntityType> entityTypes;
    private final List<String> materials;

    public PlayerStatistics(MinecraftApi server) {
        this.server = server;
        statistic = server.getStatistics();
        entityTypes = server.getEntityTypes();
        materials = server.getMaterials();
    }

    @Override
    public List<MetricFamilySamples> collect() {
        server.getPlayers().forEach(this::collect);
        return collector.collect();
    }

    private void collect(MinecraftPlayer player) {
        getStatistics(player).forEach((stat, value) -> collector.labels(player.getName(), player.getUniqueId(), stat).set(value));
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

    private int getStatistic(MinecraftPlayer player, MinecraftPlayerStatistic statistic) {
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
