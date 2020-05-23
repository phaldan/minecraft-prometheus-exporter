package de.sldk.mc.server;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
class MinecraftApiTest {

    @Mock
    private Plugin bukkitApi;

    @Mock
    private Server bukkitServer;

    @Mock
    private BukkitScheduler bukkitScheduler;

    private MinecraftApi adapter;

    @BeforeEach
    void beforeEachTest() {
        when(bukkitApi.getServer()).thenReturn(bukkitServer);
        when(bukkitServer.getScheduler()).thenReturn(bukkitScheduler);
        adapter = new MinecraftApi(bukkitApi);
    }

    @Test
    void testScheduleSyncRepeatingTask() {
        when(bukkitScheduler.scheduleSyncRepeatingTask(eq(bukkitApi), any(Runnable.class), eq(42L), eq(13L))).thenReturn(1337);
        int result = adapter.scheduleSyncRepeatingTask(() -> {}, 42L, 13L);
        assertThat(result).isEqualTo(1337);
    }

    @Test
    void testCancelScheduledTask() {
        adapter.cancelScheduledTask(1337);
        verify(bukkitScheduler, times(1)).cancelTask(eq(1337));
    }

    @Test
    void testGetWorlds(@Mock World bukkitWorld1, @Mock World bukkitWorld2) {
        when(bukkitServer.getWorlds()).thenReturn(asList(bukkitWorld1, bukkitWorld2));

        List<MinecraftWorld> worlds = adapter.getWorlds();
        assertThat(worlds).isNotEmpty();
        assertThat(worlds).containsOnly(new MinecraftWorld(bukkitWorld2), new MinecraftWorld(bukkitWorld1));
    }

    @Test
    void testCountPlayers(@Mock OfflinePlayer player) {
        OfflinePlayer[] players = { player, player, player };
        when(bukkitServer.getOfflinePlayers()).thenReturn(players);
        assertThat(adapter.countPlayers()).isEqualTo(3);
    }

    @Test
    void testGetOfflinePlayers(@Mock OfflinePlayer offlinePlayer1, @Mock OfflinePlayer offlinePlayer2, @Mock Player player1, @Mock Player player2) {
        when(offlinePlayer1.getPlayer()).thenReturn(player1);
        when(offlinePlayer2.getPlayer()).thenReturn(player2);
        when(bukkitServer.getOfflinePlayers()).thenReturn(new OfflinePlayer[] { offlinePlayer1, offlinePlayer2 });

        List<MinecraftPlayer> result = adapter.getPlayers();
        assertThat(result).isNotEmpty();
        assertThat(result).containsOnly(new MinecraftPlayer(offlinePlayer2), new MinecraftPlayer(offlinePlayer1));
    }

    @Test
    void testGetStatistics() {
        List<MinecraftPlayerStatistic> statistics = adapter.getStatistics();
        assertThat(statistics).isNotEmpty();
        assertThat(statistics).hasSize(Statistic.values().length);
    }

    @Test
    void testGetEntityTypes() {
        List<MinecraftEntityType> entityTypes = adapter.getEntityTypes();
        assertThat(entityTypes).isNotEmpty();
        assertThat(entityTypes).hasSize(EntityType.values().length);
    }

    @Test
    void testGetMaterials() {
        List<String> materials = adapter.getMaterials();
        assertThat(materials).isNotEmpty();
        assertThat(materials).hasSize(Material.values().length);
    }

    @Test
    void testGetTickDurations(@Mock Plugin plugin, @Mock ITestServer server, @Mock Logger logger) {
        TestMinecraftServer minecraftServer = new TestDedicatedServer();
        minecraftServer.longestArray[0] = 42;
        minecraftServer.longestArray[1] = 1337;
        minecraftServer.longestArray[2] = 7;
        minecraftServer.longestArray[3] = 13;
        minecraftServer.longestArray[4] = 2;
        minecraftServer.longestArray[5] = 17;
        minecraftServer.longestArray[6] = 11;
        when(server.getServer()).thenReturn(minecraftServer);
        when(plugin.getServer()).thenReturn(server);
        MinecraftApi adapter = new MinecraftApi(plugin);
        assertThat(adapter.getTickDurations())
                .isPresent()
                .get(as(LIST))
                .containsOnly(42L, 1337L, 7L, 13L, 2L, 17L, 11L);
    }

    private interface ITestServer extends Server {
        Object getServer();
    }

    private static class TestMinecraftServer {
        public final long[] longestArray = new long[7];
        public final long[] shortArray = new long[3];
        private final long[] middleArray = new long[5];
    }

    private static class TestDedicatedServer extends TestMinecraftServer {
    }
}