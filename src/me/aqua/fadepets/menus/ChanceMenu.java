package me.aqua.fadepets.menus;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.effects.Effect;
import me.aqua.fadepets.pets.PetOwner;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.utils.ItemBuilder;
import me.aqua.fadepets.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChanceMenu {

    private final PetsPlugin plugin;
    private Inventory inventory;
    private final String title;

    private int size;
    private List<Integer> slots;
    private EntityType type;

    public ChanceMenu(PetsPlugin plugin) {
        this.title = StringUtils.toColor("Pets &l»&r Upgrade");
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 27, title);
    }

    public void open(Player player, EntityType type) {
        this.type = type;
        loadSlots();

        this.inventory = Bukkit.createInventory(null, size, title);

        buildPanes();
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
        PlayerData data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());

        buildUpgrades(data);
        buildButton(data);

        inventory.setItem(4, updateIcon(data));
    }

    private void buildUpgrades(PlayerData data) {
        int level = data.getPet(type).getLevel();
        List<Effect> effects = data.getPet(type).getPet().getEffect();

        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            int slot = slots.get(i);
            ItemStack item;

            if (effect.getLevel() <= level) {
                item = new ItemBuilder(Material.BEACON, 0)
                        .setAmount((i+1))
                        .setName("&bLevel " + effect.getLevel() + " Perk")
                        .setLore("&7" + effect.getDesc(),
                                "",
                                "&b&lUNLOCKED")
                        .getStack();
            } else {
                item = new ItemBuilder(Material.BARRIER, 0)
                        .setAmount((i+1))
                        .setName("&cLevel " + effect.getLevel() + " Perk")
                        .setLore("&7" + effect.getDesc(),
                                "",
                                "&c&lLOCKED")
                        .getStack();
            }

            inventory.setItem(slot, item);
        }
    }

    private ItemStack updateIcon(PlayerData data) {
        ItemStack clone = data.getPet(type).getPet().getIcon();

        int level = data.getPet(type).getLevel();
        return new ItemBuilder(clone)
                .setName(clone.getItemMeta().getDisplayName(), level)
                .setLore(clone.getItemMeta().getLore(), level)
                .getStack();
    }

    private void buildButton(PlayerData data) {
        PetOwner petOwner = data.getPet(type);
        int chance = petOwner.getPet().getEffect().get(petOwner.getLevel()).getChance();
        Effect effect = petOwner.getPet().getEffect().get(petOwner.getLevel());
        int required = (int) (effect.getPrice() + (effect.getPrice() * (petOwner.getUpgrades() * 0.05)));
        String format = NumberFormat.getInstance().format(required);

        ItemStack back = new ItemBuilder(Material.NETHER_STAR, 0)
                .setName("&bUpgrade Pet")
                .setLore("&7Upgrade your pet and receive better",
                        "&7pet perks each level it increases.",
                        "",
                        "&bSuccess Chance: &f" + chance + "%",
                        "&bPrice: &f" + format + " EXP")
                .getStack();

        inventory.setItem(inventory.getSize() - 5, back);
    }

    private void buildPanes() {
        ItemStack pane = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .setName(" ")
                .getStack();

        ItemStack gray = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(" ")
                .getStack();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (slots.contains(i)) {
                inventory.setItem(i, gray);
            } else {
                inventory.setItem(i, pane);
            }
        }
    }

    private void loadSlots() {
        int effects = plugin.getPetManager().getPet(type).getEffect().size();

        if (0 < effects && effects <= 5) {
            slots = Arrays.asList(11, 12, 13, 14, 15);
            size = 27;

        } else if (5 < effects && effects <= 10) {
            slots = Arrays.asList(
                    11, 12, 13, 14, 15,
                    20, 21, 22, 23, 24);

            size = 36;

        } else if (10 < effects && effects <= 15) {
            slots = Arrays.asList(
                    11, 12, 13, 14, 15,
                    20, 21, 22, 23, 24,
                    29, 30, 31, 32, 33);

            size = 45;

        } else {
            slots = new ArrayList<>();
            size = 54;
        }
    }
}
