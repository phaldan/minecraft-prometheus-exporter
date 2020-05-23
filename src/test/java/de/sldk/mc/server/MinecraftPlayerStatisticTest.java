package de.sldk.mc.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.bukkit.Statistic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MinecraftPlayerStatisticTest {

    public static final Statistic UNTYPED = Statistic.ANIMALS_BRED;
    public static final Statistic ITEM = Statistic.CRAFT_ITEM;
    public static final Statistic ENTITY = Statistic.KILL_ENTITY;
    public static final Statistic BLOCK = Statistic.MINE_BLOCK;

    @Test
    void testGetName() {
        MinecraftPlayerStatistic playerStatistic = new MinecraftPlayerStatistic(Statistic.ANIMALS_BRED);
        assertThat(playerStatistic.getName()).isEqualTo("ANIMALS_BRED");
    }

    static Stream<Arguments> testGetType() {
        return Stream.of(
                arguments(UNTYPED, "UNTYPED"),
                arguments(ITEM, "ITEM"),
                arguments(ENTITY, "ENTITY"),
                arguments(BLOCK, "BLOCK")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testGetType(Statistic statistic, String name) {
        MinecraftPlayerStatistic playerStatistic = new MinecraftPlayerStatistic(statistic);
        assertThat(playerStatistic.getType()).isEqualTo(name);
    }

    static Stream<Arguments> testIsUntyped() {
        return Stream.of(
                arguments(UNTYPED, true),
                arguments(ITEM, false),
                arguments(ENTITY, false),
                arguments(BLOCK, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testIsUntyped(Statistic statistic, boolean isUntyped) {
        MinecraftPlayerStatistic playerStatistic = new MinecraftPlayerStatistic(statistic);
        assertThat(playerStatistic.isUntyped()).isEqualTo(isUntyped);
    }

    static Stream<Arguments> testIsEntity() {
        return Stream.of(
                arguments(UNTYPED, false),
                arguments(ITEM, false),
                arguments(ENTITY, true),
                arguments(BLOCK, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testIsEntity(Statistic statistic, boolean isEntity) {
        MinecraftPlayerStatistic playerStatistic = new MinecraftPlayerStatistic(statistic);
        assertThat(playerStatistic.isEntity()).isEqualTo(isEntity);
    }

    static Stream<Arguments> testIsMaterial() {
        return Stream.of(
                arguments(UNTYPED, false),
                arguments(ITEM, true),
                arguments(ENTITY, false),
                arguments(BLOCK, true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testIsMaterial(Statistic statistic, boolean isMaterial) {
        MinecraftPlayerStatistic playerStatistic = new MinecraftPlayerStatistic(statistic);
        assertThat(playerStatistic.isMaterial()).isEqualTo(isMaterial);
    }

    @Test
    void testEquals() {
        MinecraftPlayerStatistic statistic1 = new MinecraftPlayerStatistic(UNTYPED);
        MinecraftPlayerStatistic statistic2 = new MinecraftPlayerStatistic(UNTYPED);
        assertThat(statistic1).isEqualTo(statistic2);
    }

    @Test
    void testHashCode() {
        MinecraftPlayerStatistic statistic1 = new MinecraftPlayerStatistic(UNTYPED);
        MinecraftPlayerStatistic statistic2 = new MinecraftPlayerStatistic(UNTYPED);
        assertThat(statistic1.hashCode()).isEqualTo(statistic2.hashCode());
    }

    @Test
    void testToString() {
        MinecraftPlayerStatistic statistic1 = new MinecraftPlayerStatistic(UNTYPED);
        assertThat(statistic1.toString()).isEqualTo("MinecraftPlayerStatistic{statistic=ANIMALS_BRED}");
    }
}