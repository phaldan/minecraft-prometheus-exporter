package de.sldk.mc;

import static org.mockito.Mockito.*;

import de.sldk.mc.metrics.Metric;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricRegistryTest {

    private MetricRegistry registry;

    @Mock
    private CollectorRegistry collectorRegistry;

    @Mock
    private PrometheusMeterRegistry meterRegistry;

    @BeforeEach
    void beforeEachTest() {
        when(meterRegistry.getPrometheusRegistry()).thenReturn(collectorRegistry);
        registry = new MetricRegistry(meterRegistry);
    }

    @Test
    void testRegisterForMetricMultipleTimesWithSameMetric(@Mock Metric metric) {
        registry.register(metric);
        registry.register(metric);

        verify(metric, times(1)).enable();
        verify(collectorRegistry, times(1)).register(eq(metric));
    }

    @Test
    void testRegisterForMeterBinderMultipleTimesWithSameMetric(@Mock MeterBinder meterBinder) {
        registry.register(meterBinder);
        registry.register(meterBinder);

        verify(meterBinder, times(1)).bindTo(eq(meterRegistry));
    }

    @Test
    void testClear(@Mock MeterBinder meterBinder, @Mock Metric metric) {
        registry.register(meterBinder);
        registry.register(metric);
        registry.clear();
        registry.register(meterBinder);
        registry.register(metric);

        verify(meterBinder, times(2)).bindTo(eq(meterRegistry));
        verify(metric, times(2)).enable();
        verify(metric, times(1)).disable();
        verify(collectorRegistry, times(2)).register(eq(metric));
    }
}