package me.aqua.fadepets.listeners.effects;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.player.PlayerManager;
import me.aqua.fadepets.utils.ExperienceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public record ExpListener(PetsPlugin plugin) implements Listener {

@EventHandler
public void onEvent(EntityDeathEvent event) {
if (event.getDroppedExp() > 0) {
Player player = event.getEntity().getKiller();

if (player != null) {
PlayerManager manager = plugin.getPlayerManager();

if (manager.hasData(player.getUniqueId())) {
PlayerData data = manager.getPlayerData(player.getUniqueId());
    double boost = data.getExpBoost() / 100.0;
    int boostAmount = (int) (event.getDroppedExp() * boost);

ExperienceManager expManager = new ExperienceManager(player);
expManager.setTotalExperience(expManager.getTotalExperience() + boostAmount);
}
}
}
}
}
