package me.aqua.fadepets.pets;

import me.aqua.fadepets.effects.Effect;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Pet(EntityType type, int level, int price, int slot, List<Effect> effects, ItemStack icon, int renamePrice) {

    public int getSlot() {
        return slot;
    }

    public EntityType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getRenamePrice() {
        return renamePrice;
    }

    public List<Effect> getEffect() {
        return effects;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
