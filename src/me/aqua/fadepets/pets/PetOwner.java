package me.aqua.fadepets.pets;

public class PetOwner {

    private final Pet pet;
    private int level;
    private int upgrades;

    public PetOwner(int level, Pet pet, int upgrades) {
        this.level = level;
        this.pet = pet;
        this.upgrades = upgrades;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public Pet getPet() {
        return pet;
    }

    public int getMaxLevel() {
        return pet.getEffect().size();
    }

    public int getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(int upgrades) {
        this.upgrades = upgrades;
    }
}
