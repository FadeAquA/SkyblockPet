package me.aqua.fadepets.listeners.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.pets.PetService;
import me.aqua.fadepets.player.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public record SummonListener(PetsPlugin plugin) implements Listener {

    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Inventory menu = plugin.getSummonMenu().getInventory();

        if (inventory != null && event.getView().getTitle().equals(plugin.getSummonMenu().getTitle()) && inventory.getSize() == menu.getSize()) {
            event.setCancelled(true);

            PetManager petManager = plugin.getPetManager();
            Player player = (Player) event.getWhoClicked();

            if (petManager.isPetSlot(event.getSlot())) {
                EntityType entity = petManager.getEntityFromSlot(event.getSlot());
                PlayerData data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());

                if (data.hasOwnership(entity)) {
                    PetService service = new PetService(plugin, player, entity);

                    // add / remove the summon from a pet
                    if (data.isSummoned(entity)) service.removeSummonPet();
                    else service.summonPet();

                    return;
                }
            }

            if (event.getSlot() == menu.getSize() - 5) {
                plugin.getPetMenu().open(player);
            }
        }
    }
}
