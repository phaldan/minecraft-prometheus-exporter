package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorAssertion.sample;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import de.sldk.mc.server.MinecraftApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TickDurationMedianCollectorTest {

    private TickDurationMedianCollector metric;

    @Mock
    private MinecraftApi adapter;

    @BeforeEach
    void beforeEachTest() {
        metric = new TickDurationMedianCollector(adapter);
    }

    @ParameterizedTest
    @MethodSource
    void testCollect(List<Long> times, double expected) {
        when(adapter.getTickDurations()).thenReturn(Optional.of(times));

        assertThat(metric).hasOnly(sample("mc_tick_duration_median", expected));
    }

    static Stream<Arguments> testCollect() {
        return Stream.of(
                arguments(asList(19L, 5L, 11L), 11),
                arguments(asList(4L, 1L, 3L, 2L), 2.5));
    }

    @Test
    void testCollectWithAbsentTimes() {
        when(adapter.getTickDurations()).thenReturn(Optional.empty());

        assertThat(metric).hasOnly(sample("mc_tick_duration_median", -1));
    }

    @Test
    void testCollectWithEmptyTimes() {
        when(adapter.getTickDurations()).thenReturn(Optional.of(emptyList()));

        assertThat(metric).hasOnly(sample("mc_tick_duration_median", -1));
    }
}