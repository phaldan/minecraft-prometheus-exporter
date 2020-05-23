package de.sldk.mc.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinecraftEntityTest {

    private final static Entity BUKKIT_ENTITY;

    static {
        BUKKIT_ENTITY = mock(Entity.class);
        when(BUKKIT_ENTITY.getType()).thenReturn(EntityType.BAT);
    }

    private MinecraftEntity entity;

    @BeforeEach
    void beforeEachTest(@Mock Entity entityMock) {
        when(entityMock.getType()).thenReturn(EntityType.BAT);
        entity = new MinecraftEntity(entityMock);
    }

    @Test
    void testGetType() {
        assertThat(entity.getType()).isEqualTo(new MinecraftEntityType(EntityType.BAT));
    }

    @Test
    void testEquals() {
        assertThat(entity).isEqualTo(new MinecraftEntity(BUKKIT_ENTITY));
    }

    @Test
    void testHashCode() {
        assertThat(entity.hashCode()).isEqualTo(new MinecraftEntity(BUKKIT_ENTITY).hashCode());
    }

    @Test
    void testToString() {
        assertThat(entity.toString()).isEqualTo("MinecraftEntity{type=MinecraftEntityType{type=BAT}}");
    }
}