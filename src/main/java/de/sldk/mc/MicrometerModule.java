package de.sldk.mc;

import static java.util.Arrays.asList;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class MicrometerModule {

    private static final String SHARED_GROUP = "micrometer_jvm";

    PrometheusMeterRegistry prometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, new CollectorRegistry(), Clock.SYSTEM);
    }

    Map<String, List<MeterBinder>> meterBinders() {
        Map<String, List<MeterBinder>> binders = new HashMap<>();
        Stream.of(
            jvmGcMetrics(),
            jvmMemoryMetrics(),
            jvmThreadMetrics(),
            classLoaderMetrics()
        ).forEach(entry -> swapKeyWithValue(binders, entry));
        return binders;
    }

    private void swapKeyWithValue(Map<String, List<MeterBinder>> binders, Entry<MeterBinder, List<String>> entry) {
        entry.getValue().stream()
            .map(group -> binders.computeIfAbsent(group, key -> new ArrayList<>()))
            .forEach(l -> l.add(entry.getKey()));
    }

    private Entry<MeterBinder, List<String>> jvmGcMetrics() {
        return new SimpleEntry<>(new JvmGcMetrics(), asList(SHARED_GROUP, "micrometer_jvm_gc"));
    }

    private Entry<MeterBinder, List<String>> jvmMemoryMetrics() {
        return new SimpleEntry<>(new JvmMemoryMetrics(), asList(SHARED_GROUP, "micrometer_jvm_memory"));
    }

    private Entry<MeterBinder, List<String>> jvmThreadMetrics() {
        return new SimpleEntry<>(new JvmThreadMetrics(), asList(SHARED_GROUP, "micrometer_jvm_threads"));
    }

    private Entry<MeterBinder, List<String>> classLoaderMetrics() {
        return new SimpleEntry<>(new ClassLoaderMetrics(), asList(SHARED_GROUP, "micrometer_jvm_class_loader"));
    }
}
