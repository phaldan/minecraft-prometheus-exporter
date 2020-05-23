package de.sldk.mc.metrics;

import static java.util.Collections.emptyList;
import static java.util.Collections.list;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.NoSuchElementException;

public class CollectorRegistryAssertion extends AbstractAssert<CollectorRegistryAssertion, CollectorRegistry> {

    private CollectorRegistryAssertion(CollectorRegistry actual) {
        super(actual, CollectorRegistryAssertion.class);
    }

    public static CollectorRegistryAssertion assertThat(CollectorRegistry actual) {
        return new CollectorRegistryAssertion(actual);
    }

    public CollectorRegistryAssertion hasOnly(Sample... samples) {
        isNotNull();
        List<MetricFamilySamples> metricFamilies = list(actual.metricFamilySamples());
        Assertions.assertThat(metricFamilies).hasSize(1);
        MetricFamilySamples metricFamilySamples = metricFamilies.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        Assertions.assertThat(metricFamilySamples.samples).containsOnly(samples);
        return this;
    }

    public static Sample sample(String name, List<String> labels, List<String> labelValues, double value) {
        return new Sample(name, labels, labelValues, value);
    }

    public static Sample sample(String name, double value) {
        return new Sample(name, emptyList(), emptyList(), value);
    }
}
