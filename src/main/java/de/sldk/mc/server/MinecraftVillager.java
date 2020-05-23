package de.sldk.mc.server;

import org.bukkit.entity.Villager;

import java.util.Objects;

public class MinecraftVillager {

    private final Villager villager;

    MinecraftVillager(Villager villager) {
        this.villager = villager;
    }

    /**
     * @return Type, e.g. 'desert', 'plains' ({@link org.bukkit.entity.Villager.Type})
     */
    public String getVillagerType() {
        return villager.getVillagerType().getKey().getKey();
    }

    /**
     * @return Profession, e.g. 'fisherman', 'farmer', or 'none' ({@link org.bukkit.entity.Villager.Profession})
     */
    public String getProfession() {
        return villager.getProfession().getKey().getKey();
    }

    /**
     * @return Level ({@link Villager#getVillagerLevel()})
     */
    public int getVillagerLevel() {
        return villager.getVillagerLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftVillager that = (MinecraftVillager) o;
        return villager.equals(that.villager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(villager);
    }

    @Override
    public String toString() {
        return "MinecraftVillager{" +
                "villager=" + villager +
                '}';
    }
}
