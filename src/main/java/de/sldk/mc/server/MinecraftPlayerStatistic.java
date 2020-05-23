package de.sldk.mc.server;

import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class MinecraftPlayerStatistic {

    private static final List<Type> MATERIAL_TYPES = asList(Type.BLOCK, Type.ITEM);

    private final Statistic statistic;

    MinecraftPlayerStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public String getName() {
        return statistic.name();
    }

    public String getType() {
        return statistic.getType().name();
    }

    public boolean isUntyped() {
        return statistic.getType().equals(Type.UNTYPED);
    }

    public boolean isEntity() {
        return statistic.getType().equals(Type.ENTITY);
    }

    public boolean isMaterial() {
        return MATERIAL_TYPES.contains(statistic.getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftPlayerStatistic that = (MinecraftPlayerStatistic) o;
        return statistic == that.statistic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(statistic);
    }

    @Override
    public String toString() {
        return "MinecraftPlayerStatistic{" +
                "statistic=" + statistic +
                '}';
    }
}
