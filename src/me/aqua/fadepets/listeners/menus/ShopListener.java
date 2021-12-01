package me.aqua.fadepets.listeners.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.pets.PetService;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public record ShopListener(PetsPlugin plugin) implements Listener {

    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Inventory menu = plugin.getShopMenu().getInventory();

        if (inventory != null && event.getView().getTitle().equals(plugin.getShopMenu().getTitle()) && inventory.getSize() == menu.getSize()) {
            event.setCancelled(true);

            PetManager petManager = plugin.getPetManager();
            Player player = (Player) event.getWhoClicked();

            if (petManager.isPetSlot(event.getSlot())) {
                EntityType entity = petManager.getEntityFromSlot(event.getSlot());
                Pet pet = petManager.getPet(entity);

                PetService petService = new PetService(plugin, player, entity);
                petService.purchasePet(pet);
                return;
            }

            if (event.getSlot() == menu.getSize() - 5) {
                plugin.getPetMenu().open(player);
            }
        }
    }
}
