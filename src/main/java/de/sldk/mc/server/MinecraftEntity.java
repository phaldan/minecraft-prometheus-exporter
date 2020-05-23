package de.sldk.mc.server;

import org.bukkit.entity.Entity;

import java.util.Objects;

public class MinecraftEntity {

    private final MinecraftEntityType type;

    MinecraftEntity(Entity entity) {
        this.type = new MinecraftEntityType(entity.getType());
    }

    public MinecraftEntityType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftEntity that = (MinecraftEntity) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "MinecraftEntity{" +
                "type=" + type +
                '}';
    }
}
