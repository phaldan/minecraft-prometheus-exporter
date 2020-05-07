package de.sldk.mc;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.Test;

import static de.sldk.mc.PrometheusExporter.createServiceInstance;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PrometheusExporterTest {

    @Test
    public void test() {
        Server server = mock(Server.class);
        when(server.getScheduler()).thenReturn(mock(BukkitScheduler.class));
        Plugin plugin = mock(Plugin.class);
        when(plugin.getServer()).thenReturn(server);
        ExporterService target = createServiceInstance(plugin);
        assertNotNull(target);
    }
}