package me.aqua.fadepets.effects;

import org.bukkit.potion.PotionEffectType;

public class PotionEffect extends Effect{

    private final PotionEffectType potion;

    public PotionEffect(int level, int amount, int chance, String desc, EffectType effect, PotionEffectType potion, int price) {
        super(level, amount, chance, desc, effect, price);
        this.potion = potion;
    }

    public PotionEffectType getPotion() {
        return potion;
    }
}
