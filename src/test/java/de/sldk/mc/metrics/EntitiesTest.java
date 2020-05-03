package de.sldk.mc.metrics;

import static de.sldk.mc.metrics.CollectorAssertion.assertThat;
import static de.sldk.mc.metrics.CollectorAssertion.sample;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.sldk.mc.server.MinecraftApi;
import de.sldk.mc.server.MinecraftEntity;
import de.sldk.mc.server.MinecraftEntityType;
import de.sldk.mc.server.MinecraftWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@ExtendWith(MockitoExtension.class)
class EntitiesTest {

	private static final String METRIC_NAME = "mc_entities_total";

	private static final List<String> METRIC_LABELS = asList("world", "type", "alive", "spawnable");

	private static final String WORLD_NAME = "world_name";

	private Entities entitiesMetric;

	@Mock
	private MinecraftWorld world;

	@BeforeEach
	void beforeEachTest(@Mock MinecraftApi adapter) {
		when(adapter.getWorlds()).thenReturn(singletonList(world));
		entitiesMetric = new Entities(adapter);
	}

	@Test
	void givenTypedEntitiesExpectCorrectCount() {
		final long numOfPigs = 2;
		final long numOfHorses = 3;
		final long numOfOrbs = 5;
		final long numOfChicken = 7;
		final long numOfMinecarts = 11;
		List<MinecraftEntity> mockedEntities = new ArrayList<>();
		mockedEntities.addAll(mockEntities(numOfPigs, "pig", true, true));
		mockedEntities.addAll(mockEntities(numOfHorses, "horse", true, true));
		mockedEntities.addAll(mockEntities(numOfOrbs, "experience_orb", false, true));
		mockedEntities.addAll(mockEntities(numOfChicken, "chicken", true, true));
		mockedEntities.addAll(mockEntities(numOfMinecarts, "minecart", false, true));
		Collections.shuffle(mockedEntities);

		when(world.getName()).thenReturn(WORLD_NAME);
		when(world.getEntities()).thenReturn(mockedEntities);

		assertThat(entitiesMetric).hasOnly(
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "pig", "true", "true"), numOfPigs),
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "horse", "true", "true"), numOfHorses),
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "experience_orb", "false", "true"), numOfOrbs),
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "chicken", "true", "true"), numOfChicken),
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "minecart", "false", "true"), numOfMinecarts));
	}

	@Test
	void expectArmorStandAliveToBeFalse() {
		final String worldName = "world_name";
		final long numOfArmorStands = 11;
		List<MinecraftEntity> mockedEntities = new ArrayList<>(mockEntities(numOfArmorStands, "armor_stand", true, true));

		when(world.getName()).thenReturn(worldName);
		when(world.getEntities()).thenReturn(mockedEntities);

		assertThat(entitiesMetric).hasOnly(
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "armor_stand", "false", "true"), numOfArmorStands));
	}

	@Test
	void givenUnknownTypeExpectNoError() {
		final String worldName = "world_name";
		final long numOfUnknowns = 33;
		List<MinecraftEntity> mockedEntities = new ArrayList<>(mockEntities(numOfUnknowns, "UNKNOWN", false, false));

		when(world.getName()).thenReturn(worldName);
		when(world.getEntities()).thenReturn(mockedEntities);

		assertThat(entitiesMetric).hasOnly(
				sample(METRIC_NAME, METRIC_LABELS, asList(WORLD_NAME, "UNKNOWN", "false", "false"), numOfUnknowns));
	}

	private List<MinecraftEntity> mockEntities(long count, String type, boolean alive, boolean spawnable) {
		MinecraftEntityType t = mock(MinecraftEntityType.class);
		when(t.getName()).thenReturn(type);
		when(t.isAlive()).thenReturn(alive);
		when(t.isSpawnable()).thenReturn(spawnable);
		return LongStream.range(0, count).mapToObj(i -> mockEntity(t)).collect(Collectors.toList());
	}

	private MinecraftEntity mockEntity(MinecraftEntityType type) {
		MinecraftEntity e = mock(MinecraftEntity.class);
		when(e.getType()).thenReturn(type);
		return e;
	}
}
