package de.sldk.mc.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.bukkit.entity.Villager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinecraftVillagerTest {

    @Mock
    private Villager bukkitVillager;

    private MinecraftVillager villager;

    @BeforeEach
    void beforeEachTest() {
        villager = new MinecraftVillager(bukkitVillager);
    }

    @Test
    void testGetVillagerType() {
        when(bukkitVillager.getVillagerType()).thenReturn(Villager.Type.DESERT);
        assertThat(villager.getVillagerType()).isEqualTo("desert");
    }

    @Test
    void testGetProfession() {
        when(bukkitVillager.getProfession()).thenReturn(Villager.Profession.FARMER);
        assertThat(villager.getProfession()).isEqualTo("farmer");
    }

    @Test
    void testGetVillagerLevel() {
        when(bukkitVillager.getVillagerLevel()).thenReturn(7);
        assertThat(villager.getVillagerLevel()).isEqualTo(7);
    }

    @Test
    void testEquals() {
        MinecraftVillager villager1 = new MinecraftVillager(bukkitVillager);
        MinecraftVillager villager2 = new MinecraftVillager(bukkitVillager);
        assertThat(villager1).isEqualTo(villager2);
    }

    @Test
    void testHashCode() {
        MinecraftVillager villager1 = new MinecraftVillager(bukkitVillager);
        MinecraftVillager villager2 = new MinecraftVillager(bukkitVillager);
        assertThat(villager1.hashCode()).isEqualTo(villager2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(villager.toString()).isEqualTo("MinecraftVillager{villager=bukkitVillager}");
    }
}