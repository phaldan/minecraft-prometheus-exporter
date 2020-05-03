package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorAssertion.sample;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftPlayer;
import de.sldk.mc.server.MinecraftPlayerStatistic;
import io.prometheus.client.Collector.MetricFamilySamples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerStatisticsTest {

    private static final List<String> LABELS = asList("player_name", "player_uid", "statistic");

    private PlayerStatistics metric;

    @Mock
    private MinecraftApi server;

    private List<MinecraftPlayerStatistic> statistics;

    private List<MinecraftEntityType> entityTypes;

    private List<String> materials;

    @BeforeEach
    void beforeEachTest() {
        statistics = new ArrayList<>();
        when(server.getStatistics()).thenReturn(statistics);
        entityTypes = new ArrayList<>();
        when(server.getEntityTypes()).thenReturn(entityTypes);
        materials = new ArrayList<>();
        when(server.getMaterials()).thenReturn(materials);

        metric = new PlayerStatistics(server);
    }

    @Test
    void doCollectWithUntypedStatistic(
            @Mock MinecraftPlayer player,
            @Mock MinecraftPlayerStatistic statistic1,
            @Mock MinecraftPlayerStatistic statistic2) {
        setupUntyped(statistic1, "rage_quit");
        when(player.getStatistic(eq(statistic1))).thenReturn(7);
        setupUntyped(statistic2, "sleepy");

        when(player.getName()).thenReturn("John");
        when(player.getUniqueId()).thenReturn("this-is-a-unique-id");
        when(server.getPlayers()).thenReturn(singletonList(player));

        assertThat(metric).hasOnly(
                createSample(7,"John", "this-is-a-unique-id", "rage_quit"),
                createSample(0,"John", "this-is-a-unique-id", "sleepy"));
    }

    private void setupUntyped(MinecraftPlayerStatistic statistic, String name) {
        when(statistic.getName()).thenReturn(name);
        when(statistic.isUntyped()).thenReturn(true);
        statistics.add(statistic);
    }

    @Test
    void doCollectWithEntityStatistic(
            @Mock MinecraftPlayer player,
            @Mock MinecraftPlayerStatistic statistic1,
            @Mock MinecraftPlayerStatistic statistic2,
            @Mock MinecraftEntityType entityType) {
        setupEntity(statistic1, "trade_entity");
        when(player.getStatistic(statistic1, entityType)).thenReturn(11);
        entityTypes.add(entityType);
        setupEntity(statistic2, "drop_entity");

        when(player.getName()).thenReturn("John");
        when(player.getUniqueId()).thenReturn("this-is-a-unique-id");
        when(server.getPlayers()).thenReturn(singletonList(player));

        assertThat(metric).hasOnly(
                createSample(11,"John", "this-is-a-unique-id", "trade_entity"),
                createSample(0,"John", "this-is-a-unique-id", "drop_entity"));
    }

    private void setupEntity(MinecraftPlayerStatistic statistic, String name) {
        when(statistic.getName()).thenReturn(name);
        when(statistic.isEntity()).thenReturn(true);
        statistics.add(statistic);
    }

    @Test
    void doCollectWithMaterialStatistic(
            @Mock MinecraftPlayer player,
            @Mock MinecraftPlayerStatistic statistic1,
            @Mock MinecraftPlayerStatistic statistic2) {
        setupMaterial(statistic1, "knock_item_over");
        when(player.getStatistic(statistic1, "dark_matter")).thenReturn(13);
        materials.add("dark_matter");
        setupMaterial(statistic2, "loose_item");

        when(player.getName()).thenReturn("John");
        when(player.getUniqueId()).thenReturn("this-is-a-unique-id");
        when(server.getPlayers()).thenReturn(singletonList(player));

        assertThat(metric).hasOnly(
                createSample(13,"John", "this-is-a-unique-id", "knock_item_over"),
                createSample(0,"John", "this-is-a-unique-id", "loose_item"));
    }

    private void setupMaterial(MinecraftPlayerStatistic statistic, String name) {
        when(statistic.getName()).thenReturn(name);
        when(statistic.isMaterial()).thenReturn(true);
        statistics.add(statistic);
    }

    @Test
    void doCollectWithUnknownStatistic(
            @Mock MinecraftPlayer player1,
            @Mock MinecraftPlayer player2,
            @Mock MinecraftPlayerStatistic statistic) {
        when(statistic.getName()).thenReturn("unknown_type");
        statistics.add(statistic);

        when(player1.getName()).thenReturn("John");
        when(player1.getUniqueId()).thenReturn("this-is-a-unique-id");
        when(player2.getName()).thenReturn("Doe");
        when(player2.getUniqueId()).thenReturn("very-very-unique-id");
        when(server.getPlayers()).thenReturn(asList(player1, player2));

        assertThat(metric).hasOnly(
                createSample(0,"John", "this-is-a-unique-id", "unknown_type"),
                createSample(0,"Doe", "very-very-unique-id", "unknown_type"));
    }

    private MetricFamilySamples.Sample createSample(double value, String... labelValues) {
        return sample("mc_player_statistic", LABELS, asList(labelValues), value);
    }
}