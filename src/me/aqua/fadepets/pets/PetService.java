package me.aqua.fadepets.pets;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.effects.Effect;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.utils.ExperienceManager;
import me.aqua.fadepets.utils.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class PetService {

    private final Player player;
    private final EntityType type;
    private final PlayerData data;
    private final PetsPlugin plugin;

    public PetService(PetsPlugin plugin, Player player, EntityType type) {
        this.plugin = plugin;
        this.player = player;
        this.type = type;
        this.data = plugin.getPlayerManager().getPlayerData(player.getUniqueId());
    }

    public void summonPet() {
        if (player.isOnGround()) {
            int limit = getSummonLimit();

            if (data.getSummoned().size() < limit) {
                Entity entity = plugin.getLivingManager().spawnPet(type, player);
                data.addSummon(entity);

                plugin.getPlayerManager().addOnlineData(player.getUniqueId());

                String name = StringUtils.capEntityName(type);
                player.sendMessage(StringUtils.toColor("&b&l[!] &bYou have summoned your &3&n" + name + " Pet&r&b!"));

                player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 25, 25);
                plugin.getSummonMenu().open(player);
                return;
            }

            player.sendMessage(StringUtils.toColor("&c&l[!] &cYou have reached your summon limit!"));
            player.closeInventory();
            return;
        }

        player.sendMessage(StringUtils.toColor("&c&l[!] &cYou must be on the ground to summon!"));
        player.closeInventory();
    }

    public void removeSummonPet() {
        data.removeSummon(type);

        if (data.getSummoned().isEmpty()) {
            plugin.getPlayerManager().removeOnline(data);
        }

        player.sendMessage(StringUtils.toColor("&e&l[!] &eYou have unsummoned your pet!"));
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 25, 25);
        plugin.getSummonMenu().open(player);
    }

    public void updatePet() {
        PetOwner petOwner = data.getPet(type);

        if (petOwner.getLevel() < petOwner.getMaxLevel()) {
            ExperienceManager expManager = new ExperienceManager(player);
            Effect effect = petOwner.getPet().getEffect().get(petOwner.getLevel());
            int exp = expManager.getTotalExperience();
            int required = (int) (effect.getPrice() + (effect.getPrice() * (petOwner.getUpgrades() * 0.05)));

            if (exp >= required) {
                expManager.setTotalExperience(exp - required);
                player.sendMessage(StringUtils.toColor("&c&l- " + required + " EXP"));

                int chance = ThreadLocalRandom.current().nextInt(100);

                if (chance < effect.getChance()) {
                    // upgrade pet
                    petOwner.setLevel(petOwner.getLevel() + 1);
                    petOwner.setUpgrades(0);

                    if (data.isSummoned(petOwner.getPet().getType())) {
                        data.updateEffect(petOwner.getPet(), petOwner.getLevel(), false);
                    }

                    String name = StringUtils.capEntityName(type);
                    player.sendMessage(StringUtils.toColor("&b&l[!] &bYour &3&n" + name
                            + " pet&r&b increased to &3&lLevel " + petOwner.getLevel() + "&r&b!"));

                    player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 3);

                    if (petOwner.getMaxLevel() == petOwner.getLevel()) {
                        player.closeInventory();
                    } else {
                        plugin.getChanceMenu().open(player, type);
                    }
                    return;
                }
                petOwner.setUpgrades(petOwner.getUpgrades() + 1);
                plugin.getChanceMenu().open(player, type);
                player.sendMessage(StringUtils.toColor("&c&l[!] &cYour pet upgrade failed!"));
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2, 1);
                return;
            }

            player.sendMessage(StringUtils.toColor("&c&l[!] &cYou do not have enough experience!"));
            player.closeInventory();
        }
    }

    public void purchasePet(Pet pet) {
        if (data.hasOwnership(type)) {
            return;
        }

        ExperienceManager expManager = new ExperienceManager(player);
        int exp = expManager.getTotalExperience();

        if (exp >= pet.getPrice()) {
            expManager.setTotalExperience(exp - pet.getPrice());

            PetOwner petOwner = new PetOwner(1, pet, 0);
            data.addOwnership(type, petOwner);

            String name = StringUtils.capEntityName(pet.getType());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
            player.sendMessage(StringUtils.toColor("&e&l[!] Congratulations! &eYou now own a &6&n"
                    + name + " Pet&r&e!"));
        } else {
            player.sendMessage(StringUtils.toColor("&c&l[!] &cYou do not have enough experience!"));
        }

        player.closeInventory();
    }

    private int getSummonLimit() {
        for (int i = 3; i > 0; i--) {
            if (player.hasPermission("pets.limit." + i))
                return i;
        }

        return 1;
    }
}
