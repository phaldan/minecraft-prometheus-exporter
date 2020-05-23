package de.sldk.mc.server;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinecraftWorldTest {

    @Mock
    private World bukkitWorld;

    private MinecraftWorld world;

    @BeforeEach
    void beforeEachTest() {
        world = new MinecraftWorld(bukkitWorld);
    }

    @Test
    void testGetName() {
        when(bukkitWorld.getName()).thenReturn("nether");
        assertThat(world.getName()).isEqualTo("nether");
    }

    @Test
    void testCountPlayers(@Mock Player bukkitPlayer) {
        when(bukkitWorld.getPlayers()).thenReturn(singletonList(bukkitPlayer));
        assertThat(world.countPlayers()).isEqualTo(1);
    }

    @Test
    void testCountLoadedChunks(@Mock Chunk bukkitChunk) {
        when(bukkitWorld.getLoadedChunks()).thenReturn(new Chunk[]{ bukkitChunk, bukkitChunk });
        assertThat(world.countLoadedChunks()).isEqualTo(2);
    }

    @Test
    void testGetVillager(@Mock Villager villager1, @Mock Villager villager2) {
        when(bukkitWorld.getEntitiesByClass(eq(Villager.class))).thenReturn(asList(villager1, villager2));
        assertThat(world.getVillager()).isNotEmpty();
        assertThat(world.getVillager()).containsOnly(new MinecraftVillager(villager2), new MinecraftVillager(villager1));
    }

    @Test
    void testGetEntities(@Mock Entity entity1, @Mock Entity entity2) {
        when(bukkitWorld.getEntities()).thenReturn(asList(entity1, entity2));
        assertThat(world.getEntities()).isNotEmpty();
        assertThat(world.getEntities()).containsOnly(new MinecraftEntity(entity2), new MinecraftEntity(entity1));
    }

    @Test
    void testEquals() {
        MinecraftWorld world1 = new MinecraftWorld(bukkitWorld);
        MinecraftWorld world2 = new MinecraftWorld(bukkitWorld);
        assertThat(world1).isEqualTo(world2);
    }

    @Test
    void testHashCode() {
        MinecraftWorld world1 = new MinecraftWorld(bukkitWorld);
        MinecraftWorld world2 = new MinecraftWorld(bukkitWorld);
        assertThat(world1.hashCode()).isEqualTo(world2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(world.toString()).isEqualTo("MinecraftWorld{world=bukkitWorld}");
    }
}