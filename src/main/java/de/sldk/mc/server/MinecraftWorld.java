package de.sldk.mc.server;

import static java.util.stream.Collectors.toList;

import org.bukkit.World;
import org.bukkit.entity.Villager;

import java.util.List;
import java.util.Objects;

public class MinecraftWorld {

    private final World world;

    MinecraftWorld(World world) {
        this.world = world;
    }

    public String getName() {
        return world.getName();
    }

    public int countPlayers() {
        return world.getPlayers().size();
    }

    public int countLoadedChunks() {
        return world.getLoadedChunks().length;
    }

    public List<MinecraftVillager> getVillager() {
        return world.getEntitiesByClass(Villager.class).stream()
                .map(MinecraftVillager::new)
                .collect(toList());
    }

    public List<MinecraftEntity> getEntities() {
        return world.getEntities().stream()
                .map(MinecraftEntity::new)
                .collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftWorld that = (MinecraftWorld) o;
        return world.equals(that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world);
    }

    @Override
    public String toString() {
        return "MinecraftWorld{" +
                "world=" + world +
                '}';
    }
}
