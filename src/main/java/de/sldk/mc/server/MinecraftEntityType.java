package de.sldk.mc.server;

import org.bukkit.entity.EntityType;

import java.util.Objects;

public class MinecraftEntityType {

    private final EntityType type;

    MinecraftEntityType(EntityType type) {
        this.type = type;
    }

    public String getName() {
        try {
            return type.getKey().getKey();
        } catch (Throwable t) {
            // Note: The entity type key above was introduced in 1.14. Older implementations should fallback here.
            return type.name();
        }
    }

    public boolean isSpawnable() {
        return type.isSpawnable();
    }

    public boolean isAlive() {
        return type.isAlive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftEntityType that = (MinecraftEntityType) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "MinecraftEntityType{" +
                "type=" + type +
                '}';
    }
}
