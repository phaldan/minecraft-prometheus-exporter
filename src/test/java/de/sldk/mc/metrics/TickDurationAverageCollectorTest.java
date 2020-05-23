package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorRegistryAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorRegistryAssertion.sample;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

import java.util.Optional;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TickDurationAverageCollectorTest {

    private TickDurationAverageCollector metric;

    private CollectorRegistry registry;

    @Mock
    private MinecraftApi adapter;

    @BeforeEach
    void beforeEachTest(@Mock Plugin plugin) {
        registry = new CollectorRegistry();
        metric = new TickDurationAverageCollector(plugin, registry, adapter);
        metric.enable();
    }

    @Test
    void testCollect() {
        when(adapter.getTickDurations()).thenReturn(Optional.of(asList(3L, 4L, 2L, 1L)));

        metric.collect();

        assertThat(registry).hasOnly(sample("mc_tick_duration_average", 2.5));
    }

    @Test
    void testCollectWithAbsentDurations() {
        when(adapter.getTickDurations()).thenReturn(Optional.empty());

        metric.collect();

        assertThat(registry).hasOnly(sample("mc_tick_duration_average", -1));
    }

    @Test
    void testCollectWithEmptyDurations() {
        when(adapter.getTickDurations()).thenReturn(Optional.of(emptyList()));

        metric.collect();

        assertThat(registry).hasOnly(sample("mc_tick_duration_average", -1));
    }
}