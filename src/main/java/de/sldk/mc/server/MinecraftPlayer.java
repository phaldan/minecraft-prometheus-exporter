package de.sldk.mc.server;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MinecraftPlayer {

    private final Player player;

    MinecraftPlayer(OfflinePlayer player) {
        this.player = player.getPlayer();
    }

    public String getName() {
        return player.getName();
    }

    public String getUniqueId() {
        return player.getUniqueId().toString();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public int getStatistic(MinecraftPlayerStatistic playerStatistic) {
        Statistic statistic = Statistic.valueOf(playerStatistic.getName());
        return player.getStatistic(statistic);
    }

    public int getStatistic(MinecraftPlayerStatistic playerStatistic, MinecraftEntityType entityType) {
        Statistic statistic = Statistic.valueOf(playerStatistic.getName());
        EntityType type = EntityType.valueOf(entityType.getName());
        try {
            return player.getStatistic(statistic, type);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getStatistic(MinecraftPlayerStatistic playerStatistic, String materialName) {
        Statistic statistic = Statistic.valueOf(playerStatistic.getName());
        Material material = Material.valueOf(materialName);
        try {
            return player.getStatistic(statistic, material);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftPlayer that = (MinecraftPlayer) o;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public String toString() {
        return "MinecraftPlayer{" +
                "player=" + player +
                '}';
    }
}
