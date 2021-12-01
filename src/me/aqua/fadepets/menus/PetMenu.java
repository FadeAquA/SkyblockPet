package me.aqua.fadepets.menus;

import me.aqua.fadepets.utils.ItemBuilder;
import me.aqua.fadepets.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PetMenu {

    private Inventory inventory;
    private final String title;

    public PetMenu() {
        title = StringUtils.toColor("Pets &l»&r Main Menu");
        inventory = Bukkit.createInventory(null, 27, title);
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(null, 27, title);
        buildPanes();
        buildItems();

        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    private void buildItems() {
        ItemStack shop = new ItemBuilder(Material.EMERALD, 0)
                .setName("&b&lPet Shop")
                .setLore("&7Unlock &nNew Pets&r&7 that can help you with",
                        "&7your skyblock experience.",
                        "",
                        "&3&l» &bClick here to shop")
                .getStack();

        ItemStack summon = new ItemBuilder(Material.ENDER_EYE, 0)
                .setName("&e&lSummon Pet")
                .setLore("&7View all your &nUnlocked Pets&r&7 and summon",
                        "&7them so that they become active.",
                        "",
                        "&6&l» &eClick here to summon")
                .getStack();

        ItemStack upgrade = new ItemBuilder(Material.DIAMOND, 0)
                .setName("&a&lUpgrade Pet")
                .setLore("&7Upgrade your pets with experience and",
                        "&7unlock better perks and effects!",
                        "",
                        "&2&l» &aClick here to upgrade")
                .getStack();

        ItemStack rename = new ItemBuilder(Material.ANVIL, 0)
                .setName("&d&lRename Pet")
                .setLore("&7Rename your pets to anything you want",
                        "&7for only &n200,000 Experience&r &7per pet.",
                        "",
                        "&c&lNOTE: &7Feature currently disabled",
                        "",
                        "&5&l» &dClick here to rename pet")
                .getStack();

        inventory.setItem(10, shop);
        inventory.setItem(12, summon);
        inventory.setItem(14, upgrade);
        inventory.setItem(16, rename);
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
