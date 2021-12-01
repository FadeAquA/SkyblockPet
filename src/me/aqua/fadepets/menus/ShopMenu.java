package me.aqua.fadepets.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.player.PlayerManager;
import me.aqua.fadepets.utils.ItemBuilder;
import me.aqua.fadepets.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopMenu {

    private final PetsPlugin plugin;
    private final String title;
    private Inventory inventory;

    public ShopMenu(PetsPlugin plugin) {
        this.plugin = plugin;
        this.title = StringUtils.toColor("Pets &l»&r Shop");
        this.inventory = Bukkit.createInventory(null, 27, title);
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(null, 27, title);
        buildPanes();
        buildBack();
        buildItems(player);

        player.openInventory(inventory);
    }

    public String getTitle() {
        return title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void buildItems(Player player) {
        PetManager petManager = plugin.getPetManager();
        PlayerManager playerManager = plugin.getPlayerManager();

        for (EntityType entity : petManager.getKeys()) {
            Pet pet = petManager.getPet(entity);
            inventory.setItem(pet.getSlot(), updateIcon(pet, playerManager, player));
        }
    }

    private ItemStack updateIcon(Pet pet, PlayerManager playerManager, Player player) {
        PlayerData data = playerManager.getPlayerData(player.getUniqueId());
        ItemStack clone = pet.getIcon().clone();

        if (data.hasOwnership(pet.getType())) {
            int level = data.getPet(pet.getType()).getLevel();
            return new ItemBuilder(clone)
                    .setName(clone.getItemMeta().getDisplayName(), level)
                    .setLore(clone.getItemMeta().getLore(), level, pet.getPrice(), ActionMenu.PURCHASED)
                    .getStack();
        } else {
            return new ItemBuilder(clone)
                    .setName(clone.getItemMeta().getDisplayName(), pet.getLevel())
                    .setLore(clone.getItemMeta().getLore(), pet.getLevel(), pet.getPrice(), ActionMenu.NEED_PURCHASE)
                    .getStack();
        }
    }

    private void buildBack() {
        ItemStack back = new ItemBuilder(Material.RED_BED, 0)
                .setName("&b&lBACK TO MAIN MENU")
                .setLore("&7Click here to return to the main menu")
                .getStack();

        inventory.setItem(inventory.getSize() - 5, back);
    }

    private void buildPanes() {
        ItemStack pane = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .setName(" ")
                .getStack();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, pane);
        }
    }
}
