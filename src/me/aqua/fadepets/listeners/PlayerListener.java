package me.aqua.fadepets.listeners;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.Set;

public record PlayerListener(PetsPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerManager manager = plugin.getPlayerManager();

        if (manager.hasData(player.getUniqueId())) {
            PlayerData data = manager.getPlayerData(player.getUniqueId());
            manager.removeOnline(data);
            data.removeAllSummons();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerManager manager = plugin.getPlayerManager();

        if (manager.hasData(player.getUniqueId())) {
            PlayerData data = manager.getPlayerData(player.getUniqueId());
            data.removeAllSummons();
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerManager manager = plugin.getPlayerManager();

        if (manager.hasData(player.getUniqueId())) {
            PlayerData data = manager.getPlayerData(player.getUniqueId());

            if (data.hasAnySummoned()) {
                Set<Entity> summoned = new HashSet<>(data.getSummoned());
                data.removeAllSummons();

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Entity entity : summoned) {
                        Entity another = plugin.getLivingManager().spawnPet(entity.getType(), player);
                        data.addSummon(another);
                    }
                }, 20);
            }
        }
    }
}
