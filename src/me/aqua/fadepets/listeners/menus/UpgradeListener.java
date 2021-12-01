package me.aqua.fadepets.listeners.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.pets.PetOwner;
import me.aqua.fadepets.pets.PetService;
import me.aqua.fadepets.player.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpgradeListener implements Listener {

    private final PetsPlugin plugin;
    private final Map<UUID, EntityType> uuidMap = new HashMap<>();

    public UpgradeListener(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Inventory menu = plugin.getUpgradeMenu().getInventory();

        if (inventory != null && event.getView().getTitle().equals(plugin.getUpgradeMenu().getTitle()) && inventory.getSize() == menu.getSize()) {
            event.setCancelled(true);

            PetManager petManager = plugin.getPetManager();
            Player player = (Player) event.getWhoClicked();

            if (petManager.isPetSlot(event.getSlot())) {
                EntityType entity = petManager.getEntityFromSlot(event.getSlot());
                PlayerData data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());

                if (data.hasOwnership(entity)) {
                    PetOwner petOwner = data.getPet(entity);

                    if (petOwner.getLevel() < petOwner.getMaxLevel()) {
                        plugin.getChanceMenu().open(player, entity);
                        uuidMap.put(player.getUniqueId(), entity);
                        return;
                    }
                }
            }

            if (event.getSlot() == menu.getSize()-5) {
                plugin.getPetMenu().open(player);
            }
        }
    }

    @EventHandler
    public void onUpgrade(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Inventory menu = plugin.getChanceMenu().getInventory();

        if (inventory == null)
            return;

        if (event.getView().getTitle().equals(plugin.getChanceMenu().getTitle())) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if (uuidMap.containsKey(player.getUniqueId())) {
                EntityType type = uuidMap.get(player.getUniqueId());

                // upgrade button
                if (event.getSlot() == menu.getSize() - 5) {
                    PetService service = new PetService(plugin, player, type);
                    service.updatePet();
                }
            }
        }
    }
}
