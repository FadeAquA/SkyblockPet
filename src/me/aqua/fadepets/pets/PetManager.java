package me.aqua.fadepets.pets;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.effects.BoosterEffect;
import me.aqua.fadepets.effects.Effect;
import me.aqua.fadepets.effects.EffectType;
import me.aqua.fadepets.effects.PotionEffect;
import me.aqua.fadepets.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PetManager {

    private final Map<EntityType, Pet> petManager = new HashMap<>();
    private final Map<Integer, EntityType> shopItem = new HashMap<>();

    public PetManager(PetsPlugin plugin) {
        loadFromConfig(plugin);
    }

    public Pet getPet(EntityType entity) {
        return petManager.get(entity);
    }

    public Set<EntityType> getKeys() {
        return petManager.keySet();
    }

    public boolean isPetSlot(int slot) {
        return shopItem.containsKey(slot);
    }

    public EntityType getEntityFromSlot(int slot) {
        return shopItem.get(slot);
    }

    private void loadFromConfig(PetsPlugin plugin) {
        FileConfiguration config = plugin.getConfig();

        for (String key : config.getConfigurationSection("Pets").getKeys(false)) {
            String path = "Pets." + key + ".";
            EntityType type = EntityType.valueOf(config.getString(path + "type"));

            String value = config.getString(path + "value");
            String name = config.getString(path + "name");
            List<String> lore = config.getStringList(path + "lore");
            int price = config.getInt(path + "price");
            int slot = config.getInt(path + "slot");
            String primary = config.getString(path + "primary");
            String secondary = config.getString(path + "secondary");
            int renamePrice = config.getInt(path + "rename-price");

            List<Effect> effects = new ArrayList<>();
            int level = 1;

            for (String key2 : config.getConfigurationSection(path + "effects").getKeys(false)) {
                String path2 = "Pets." + key + ".effects." + key2 + ".";
                EffectType effectType = EffectType.valueOf(config.getString(path2 + "type"));
                String desc = config.getString(path2 + "name");
                int amount = config.getInt(path2 + "amount");
                int chance = config.getInt(path2 + "chance");
                int upgradeprice = config.getInt(path2 + "upgrade-price");

                PotionEffectType potionType = PotionEffectType.getByName(
                        config.getString(path2 + "value", "SPEED"));

                Effect effect = effectType.equals(EffectType.POTION_EFFECT) ?
                        new PotionEffect(level, amount, chance, desc, effectType, potionType, upgradeprice) :
                        new BoosterEffect(level, amount, chance, desc, effectType, upgradeprice);

                effects.add(effect); level++;
            }

            ItemBuilder icon = new ItemBuilder(new ItemStack(Material.PLAYER_HEAD));
            icon.setName(name);
            icon.setLore(lore);

            petManager.put(type, new Pet(type, 1, price, slot, effects, icon.getStack(), renamePrice));
            shopItem.put(slot, type);
        }
    }
}
