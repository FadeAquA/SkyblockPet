package me.aqua.fadepets.utils;

import com.google.common.collect.Lists;
import me.aqua.fadepets.menus.ActionMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.text.NumberFormat;
import java.util.List;

public class ItemBuilder {

    private final ItemStack item;

    /**
     * Constructs a new ItemBuilder instance
     *
     * @param material - Material type of ItemStack
     * @param data     - Data type of ItemStack
     */
    public ItemBuilder(Material material, int data) {
        this(material);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    /**
     * Constructs a new ItemBuilder instance
     *
     * @param item - ItemStack
     */
    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
    }

    /**
     * Sets the ItemStack amount
     *
     * @param amount - Amount to set ItemStack amount too
     * @return ItemBuilder
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public static String toColor(String x) {
        Validate.notNull(x);

        return ChatColor.translateAlternateColorCodes('&', x);
    }

    /**
     * Sets the name of the ItemStack
     *
     * @param name - New ItemStack name
     * @return ItemBuilder
     */
    public ItemBuilder setName(String name) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(toColor(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setName(String name, int level) {
        final ItemMeta meta = item.getItemMeta();
        name = name.replace("%level%", String.valueOf(level));
        meta.setDisplayName(toColor(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setName(String name, String level) {
        final ItemMeta meta = item.getItemMeta();
        name = name.replace("%level%", String.valueOf(level));
        meta.setDisplayName(toColor(name));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the lore of the ItemStack
     *
     * @param lore - New lore of ItemStack
     * @return ItemBuilder
     */
    public ItemBuilder setLore(final List<String> lore) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lores = Lists.newArrayList();

        for (String s : lore) {
            lores.add(toColor(s));
        }

        meta.setLore(lores);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(final List<String> lore, int level) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lores = Lists.newArrayList();

        for (String s : lore) {
            s = s.replace("%level%", String.valueOf(level));
            lores.add(toColor(s));
        }

        meta.setLore(lores);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(final List<String> lore, String level) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lores = Lists.newArrayList();

        for (String s : lore) {
            s = s.replace("%level%", String.valueOf(level));
            lores.add(toColor(s));
        }

        meta.setLore(lores);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(final List<String> lore, int level, int price, ActionMenu action) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lores = Lists.newArrayList();

        for (String s : lore) {
            s = s.replace("%level%", String.valueOf(level));
            lores.add(toColor(s));
        }

        lores.add(toColor("&8&m----------------"));
        lores.add("");

        switch(action) {
            case SUMMON: {
                lores.add(toColor("&eClick here to &lSUMMON&r&e pet"));
                break;
            }

            case UNSUMMON: {
                lores.add(toColor("&eClick here to &lUNSUMMON&r&e pet"));
                break;
            }

            case UPGRADE: {
                lores.add(toColor("&bClick here to &lUPGRADE&r&b your pet"));
                break;
            }

            case PURCHASED: {
                lores.add(toColor("&aPet is already purchased"));
                break;
            }

            case NEED_PURCHASE: {
                String format = NumberFormat.getInstance().format(price);
                lores.add(toColor("&e&lPRICE: &f" + format + " EXP"));
                lores.add(toColor("&eClick here to purchase."));
                break;
            }
        }

        meta.setLore(lores);
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Sets the lore of the ItemStack
     *
     * @param lore - New lore of ItemStack
     * @return ItemBuilder
     */
    public ItemBuilder setLore(final String... lore) {
        final ItemMeta meta = item.getItemMeta();
        List<String> lores = Lists.newArrayList();

        for (String s : lore) {
            lores.add(toColor(s));
        }

        meta.setLore(lores);
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Adds an enchantment to the ItemStack
     *
     * @param ench  - Enchantment to add
     * @param level - Level of enchantment to add
     * @return ItemBuilder
     */
    public ItemBuilder addEnchantment(final Enchantment ench, final int level) {
        item.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * Sets the color of the Leather amour
     *
     * @param color - Color of armor
     * @return ItemBuilder
     */
    public ItemBuilder setColor(final Color color) {
        if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_CHESTPLATE ||
                item.getType() == Material.LEATHER_HELMET) {
            final LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(color);
            item.setItemMeta(meta);
        } else {
            throw new IllegalArgumentException("setColor can only be used on leather armour!");
        }
        return this;
    }

    /**
     * Sets the skull owner of the ItemStack
     *
     * @param name - Skull owner
     * @return ItemBuilder
     */
    public ItemBuilder setOwner(String name) {
        SkullMeta meta = (SkullMeta) this.item.getItemMeta();
        meta.setOwner(name);
        this.item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the durability of the ItemStack
     *
     * @param durability - new durability of ItemStack
     * @return ItemBuilder
     */
    public ItemBuilder setDurability(final int durability) {
        if (durability >= Short.MIN_VALUE && durability <= Short.MAX_VALUE) {
            item.setDurability((short) durability);
        } else {
            throw new IllegalArgumentException("The durability must be a short!");
        }
        return this;
    }

    /**
     * Sets the MaterialData of the ItemStack
     *
     * @param data - new MaterialData of ItemStack
     * @return ItemBuilder
     */
    public ItemBuilder setData(MaterialData data) {
        final ItemMeta meta = item.getItemMeta();
        item.setData(data);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addUnbreaking() {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the ItemStack
     *
     * @return ItemStack
     */
    public ItemStack getStack() {
        return this.item;
    }
}
