package me.aqua.fadepets.player;

import me.aqua.fadepets.effects.Effect;
import me.aqua.fadepets.effects.EffectType;
import me.aqua.fadepets.effects.PotionEffect;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetOwner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerData {

    private final UUID uuid;

    private final Map<EntityType, PetOwner> owned = new HashMap<>();
    private final Set<Entity> summoned = new HashSet<>();

    private int moneyBoost;
    private int coinChance;
    private int expBoost;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getPlayerId() {
        return uuid;
    }

    public void addOwnership(EntityType type, PetOwner petOwner) {
        owned.put(type, petOwner);
    }

    public PetOwner getPet(EntityType type) {
        return owned.get(type);
    }

    public boolean hasOwnership(EntityType type) {
        return owned.containsKey(type);
    }

    public Set<EntityType> getAllOwnedPets() {
        return owned.keySet();
    }

    public void addSummon(Entity entity) {
        summoned.add(entity);
        PetOwner pet = getPet(entity.getType());
        updateEffect(pet.getPet(), pet.getLevel(), false);
    }

    public void removeSummon(EntityType entity) {
        Iterator<Entity> iterator = summoned.iterator();

        while (iterator.hasNext()) {
            Entity e = iterator.next();

            if (e.getType().equals(entity)) {
                iterator.remove(); e.remove();

                PetOwner pet = getPet(e.getType());
                updateEffect(pet.getPet(), pet.getLevel(), true);
            }
        }
    }

    public void removeAllSummons() {
        for (Entity e : summoned) {
            e.remove();

            PetOwner pet = getPet(e.getType());
            updateEffect(pet.getPet(), pet.getLevel(), true);
        }

        summoned.clear();
    }

    public Set<Entity> getSummoned() {
        return summoned;
    }

    public boolean isSummoned(EntityType entity) {
        for (Entity e : summoned) {
            if (e.getType().equals(entity))
                return summoned.contains(e);
        }

        return false;
    }

    public boolean hasAnySummoned() {
        return !summoned.isEmpty();
    }

    public int getExpBoost() {
        return expBoost;
    }

    public int getMoneyBoost() {
        return moneyBoost;
    }

    public int getCoinChance() {
        return coinChance;
    }

    public void setExpBoost(int expBoost) {
        this.expBoost = expBoost;
    }

    public void setMoneyBoost(int moneyBoost) {
        this.moneyBoost = moneyBoost;
    }

    public void setCoinChance(int coinChance) {
        this.coinChance = coinChance;
    }

    public void updateEffect(Pet pet, int level, boolean remove) {
        Effect effect = pet.getEffect().get(level - 1);

        if (effect.getEffect().equals(EffectType.MONEY_BOOSTER)) {
            if (remove) setMoneyBoost(0);
            else setMoneyBoost(effect.getAmount());
            return;
        }

        if (effect.getEffect().equals(EffectType.EXP_BOOSTER)) {
            if (remove) setExpBoost(0);
            else setExpBoost(effect.getAmount());
            return;
        }

        if (effect.getEffect().equals(EffectType.POTION_EFFECT)) {
            PotionEffect potionEffect = (PotionEffect) effect;
            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {

                if (remove) {
                    player.removePotionEffect(potionEffect.getPotion());
                } else {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            potionEffect.getPotion(), Integer.MAX_VALUE,
                            effect.getAmount()-1, false, false));
                }
            }
            return;
        }
        if (effect.getEffect().equals(EffectType.FLY)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                if (remove) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                } else {
                    player.setAllowFlight(true);
                }
            }
        }
    }
}
