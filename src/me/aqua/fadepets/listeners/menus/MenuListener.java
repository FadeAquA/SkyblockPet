package me.aqua.fadepets.listeners.menus;

import me.aqua.fadepets.PetsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public record MenuListener(PetsPlugin plugin) implements Listener {

    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Inventory menu = plugin.getPetMenu().getInventory();

        if (inventory != null && event.getView().getTitle().equals(plugin.getPetMenu().getTitle()) && inventory.getSize() == menu.getSize()) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if (item == null) {
                return;
            }

            if (item.getType().equals(Material.EMERALD)) {
                plugin.getShopMenu().open(player);
                return;
            }

            if (item.getType().equals(Material.ENDER_EYE)) {
                plugin.getSummonMenu().open(player);
                return;
            }

            if (item.getType().equals(Material.DIAMOND)) {
                plugin.getUpgradeMenu().open(player);
                return;
            }

            if (item.getType().equals(Material.ANVIL)) {

            }
        }
    }
}
