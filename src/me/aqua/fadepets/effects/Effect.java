package me.aqua.fadepets.effects;

public abstract class Effect {

    private final int level;
    private final int amount;
    private final int chance;
    private final int price;

    private final String desc;
    private final EffectType effect;

    public Effect(int level, int amount, int chance, String desc, EffectType effect, int price) {
        this.level = level;
        this.amount = amount;
        this.chance = chance;
        this.desc = desc;
        this.effect = effect;
        this.price = price;
    }

    public int getLevel() {
        return level;
    }

    public int getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }

    public EffectType getEffect() {
        return effect;
    }

    public int getChance() {
        return chance;
    }

    public int getPrice() {
        return price;
    }
}
