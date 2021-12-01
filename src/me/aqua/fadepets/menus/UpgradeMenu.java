package me.aqua.fadepets.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.effects.Effect;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.pets.PetOwner;
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

public class UpgradeMenu {

    private final PetsPlugin plugin;
    private final String title;
    private Inventory inventory;

    public UpgradeMenu(PetsPlugin plugin) {
        this.plugin = plugin;
        this.title = StringUtils.toColor("Pets &l»&r Choose a pet");
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
            PetOwner petOwner = data.getPet(pet.getType());

            if (petOwner.getLevel() < petOwner.getMaxLevel()) {
                int level = data.getPet(pet.getType()).getLevel();
                return new ItemBuilder(clone)
                        .setName(clone.getItemMeta().getDisplayName(), level)
                        .setLore(clone.getItemMeta().getLore(), level, pet.getPrice(), ActionMenu.UPGRADE)
                        .getStack();
            } else {
                return new ItemBuilder(clone)
                        .setName(clone.getItemMeta().getDisplayName(), "MAX")
                        .setLore(clone.getItemMeta().getLore(), "MAX")
                        .getStack();
            }

        } else {
            String name = StringUtils.capEntityName(pet.getType());
            return new ItemBuilder(Material.BARRIER, 0)
                    .setName("&c" + name + " Pet")
                    .setLore("&7You must first &nPurchase&r&7 this pet",
                            "&7before you can start using it!",
                            "",
                            "&c&lLOCKED")
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
