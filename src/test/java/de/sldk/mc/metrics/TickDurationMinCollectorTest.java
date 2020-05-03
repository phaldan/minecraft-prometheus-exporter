package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorAssertion.sample;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

import java.util.Optional;

import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TickDurationMinCollectorTest {

    private TickDurationMinCollector metric;

    private CollectorRegistry registry;

    @Mock
    private MinecraftApi adapter;

    @BeforeEach
    void beforeEachTest() {
        metric = new TickDurationMinCollector(adapter);
    }

    @Test
    void testCollect() {
        when(adapter.getTickDurations()).thenReturn(Optional.of(asList(3L, 4L, 2L, 1L)));

        assertThat(metric).hasOnly(sample("mc_tick_duration_min", 1));
    }

    @Test
    void testCollectWithAbsentDurations() {
        when(adapter.getTickDurations()).thenReturn(Optional.empty());

        assertThat(metric).hasOnly(sample("mc_tick_duration_min", -1));
    }

    @Test
    void testCollectWithEmptyDurations() {
        when(adapter.getTickDurations()).thenReturn(Optional.of(emptyList()));

        assertThat(metric).hasOnly(sample("mc_tick_duration_min", -1));
    }

}