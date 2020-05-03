package de.sldk.mc.metrics;

import static java.util.Collections.emptyList;

import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.NoSuchElementException;

public class CollectorAssertion extends AbstractAssert<CollectorAssertion, Collector> {

    private CollectorAssertion(Collector actual) {
        super(actual, CollectorAssertion.class);
    }

    public static CollectorAssertion assertThat(Collector actual) {
        return new CollectorAssertion(actual);
    }

    public CollectorAssertion hasOnly(Sample... samples) {
        isNotNull();
        List<MetricFamilySamples> metricFamilies = actual.collect();
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
