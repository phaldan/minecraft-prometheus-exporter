package de.sldk.mc.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinecraftEntityTypeTest {

    private MinecraftEntityType entityType;

    @BeforeEach
    void beforeEachTest() {
        entityType = new MinecraftEntityType(EntityType.CHICKEN);
    }

    @Test
    void testGetName() {
        assertThat(entityType.getName()).isEqualTo("chicken");
    }

    @Test
    void testIsSpawnable() {
        assertThat(entityType.isSpawnable()).isTrue();
    }

    @Test
    void testIsAlive() {
        assertThat(entityType.isAlive()).isTrue();
    }

    @Test
    void testEquals() {
        assertThat(entityType).isEqualTo(new MinecraftEntityType(EntityType.CHICKEN));
    }

    @Test
    void testHashCode() {
        assertThat(entityType.hashCode()).isEqualTo(new MinecraftEntityType(EntityType.CHICKEN).hashCode());
    }

    @Test
    void testToString() {
        assertThat(entityType.toString()).isEqualTo("MinecraftEntityType{type=CHICKEN}");
    }
}