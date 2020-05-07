package de.sldk.mc;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

import org.junit.jupiter.api.Test;

class MicrometerModuleTest {

    @Test
    void testMeterBinders() {
        MicrometerModule module = new MicrometerModule();
        assertThat(module.meterBinders())
                .size().isGreaterThan(1).returnToMap()
                .extracting("micrometer_jvm", as(LIST))
                .size().isGreaterThan(1);
    }
}