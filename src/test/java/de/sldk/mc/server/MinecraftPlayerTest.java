package de.sldk.mc.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MinecraftPlayerTest {

    public static final Statistic STATISTIC = Statistic.ANIMALS_BRED;

    @Mock
    private Player bukkitPlayer;

    private MinecraftPlayer player;

    @BeforeEach
    void beforeEachTest(@Mock OfflinePlayer offlinePlayer) {
        when(offlinePlayer.getPlayer()).thenReturn(bukkitPlayer);
        player = new MinecraftPlayer(offlinePlayer);
    }

    @Test
    void testGetName() {
        when(bukkitPlayer.getName()).thenReturn("John");
        assertThat(player.getName()).isEqualTo("John");
    }

    @Test
    void testGetUniqueId() {
        when(bukkitPlayer.getUniqueId()).thenReturn(UUID.fromString("c8024423-730f-4edd-9afc-214a00917f48"));
        assertThat(player.getUniqueId()).isEqualTo("c8024423-730f-4edd-9afc-214a00917f48");
    }

    @Test
    void testIsOnline() {
        when(bukkitPlayer.isOnline()).thenReturn(true);
        assertThat(player.isOnline()).isTrue();
    }

    @Test
    void testGetStatistic(@Mock MinecraftPlayerStatistic playerStatistic) {
        when(playerStatistic.getName()).thenReturn(STATISTIC.name());
        when(bukkitPlayer.getStatistic(eq(STATISTIC))).thenReturn(13);
        assertThat(player.getStatistic(playerStatistic)).isEqualTo(13);
    }

    @Test
    void testGetStatisticFromEntityType(@Mock MinecraftPlayerStatistic playerStatistic, @Mock MinecraftEntityType entityType) {
        when(playerStatistic.getName()).thenReturn(STATISTIC.name());
        EntityType bukkitType = EntityType.BAT;
        when(entityType.getName()).thenReturn(bukkitType.name());
        when(bukkitPlayer.getStatistic(eq(STATISTIC), eq(bukkitType))).thenReturn(7);
        assertThat(player.getStatistic(playerStatistic, entityType)).isEqualTo(7);
    }

    @Test
    void testGetStatisticFromMaterial(@Mock MinecraftPlayerStatistic playerStatistic) {
        when(playerStatistic.getName()).thenReturn(STATISTIC.name());
        Material material = Material.CHICKEN;
        when(bukkitPlayer.getStatistic(eq(STATISTIC), eq(material))).thenReturn(2);
        assertThat(player.getStatistic(playerStatistic, material.name())).isEqualTo(2);
    }

    @Test
    void testEquals(@Mock OfflinePlayer offlinePlayer) {
        when(offlinePlayer.getPlayer()).thenReturn(bukkitPlayer);
        MinecraftPlayer player1 = new MinecraftPlayer(offlinePlayer);
        MinecraftPlayer player2 = new MinecraftPlayer(offlinePlayer);
        assertThat(player1).isEqualTo(player2);
    }

    @Test
    void testHashCode(@Mock OfflinePlayer offlinePlayer, @Mock Player bukkitPlayer) {
        when(offlinePlayer.getPlayer()).thenReturn(bukkitPlayer);
        MinecraftPlayer player1 = new MinecraftPlayer(offlinePlayer);
        MinecraftPlayer player2 = new MinecraftPlayer(offlinePlayer);
        assertThat(player1.hashCode()).isEqualTo(player2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(player.toString()).isEqualTo("MinecraftPlayer{player=bukkitPlayer}");
    }
}