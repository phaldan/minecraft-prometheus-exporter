package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorRegistryAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorRegistryAssertion.sample;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.sldk.mc.server.MinecraftVillager;
import de.sldk.mc.server.MinecraftWorld;
import de.sldk.mc.server.MinecraftApi;
import io.prometheus.client.CollectorRegistry;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class VillagersTest {

	private static final String METRIC_NAME = "mc_villagers_total";

	private static final List<String> METRIC_LABELS = asList("world", "type", "profession", "level");

	private static final String WORLD_NAME = "world_name";

	private CollectorRegistry registry;

	private Villagers villagersMetric;

	@Mock
	private MinecraftWorld world;

	@BeforeEach
	void beforeEachTest(@Mock Plugin plugin) {
		registry = new CollectorRegistry();
		villagersMetric = new Villagers(plugin, registry, mock(MinecraftApi.class));
		villagersMetric.enable();
	}

	@Test
	void givenVillagersExpectCorrectCount() {
		final long numOfDesertFarmersLevel1 = 2;
		final long numOfPlainsNoneLevel2 = 3;

		List<MinecraftVillager> mockedVillagers = Stream.concat(
				mockVillagers(numOfDesertFarmersLevel1, "desert", "farmer", 1),
				mockVillagers(numOfPlainsNoneLevel2, "plains", "none", 2))
				.collect(Collectors.toList());

		when(world.getName()).thenReturn(WORLD_NAME);
		when(world.getVillager()).thenReturn(mockedVillagers);
		villagersMetric.collect(world);

		assertThat(registry).hasOnly(
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "desert", "farmer", "1"), numOfDesertFarmersLevel1),
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "plains", "none", "2"), numOfPlainsNoneLevel2));
	}

	private Stream<MinecraftVillager> mockVillagers(long count, String type, String profession, int level) {
		return LongStream.range(0, count).mapToObj(i -> mockVillager(type, profession, level));
	}

	private MinecraftVillager mockVillager(String type, String profession, int level) {
		MinecraftVillager e = mock(MinecraftVillager.class);
		when(e.getVillagerType()).thenReturn(type);
		when(e.getProfession()).thenReturn(profession);
		when(e.getVillagerLevel()).thenReturn(level);
		return e;
	}
}
