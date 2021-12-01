package me.aqua.fadepets.pathfinder;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.pets.PetOwner;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.player.PlayerManager;
import me.aqua.fadepets.utils.StringUtils;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Objects;

public class LivingManager {

    private PetsPlugin plugin;

    public LivingManager(PetsPlugin plugin) {
        followTask(plugin);
    }

    public void followTask(PetsPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            PlayerManager manager = plugin.getPlayerManager();
            Iterator<PlayerData> iterator = manager.getOnlineData().iterator();

            while (iterator.hasNext()) {
                PlayerData data = iterator.next();
                Player player = Bukkit.getPlayer(data.getPlayerId());

                if (player == null) {
                    continue;
                }

                for (Entity entity : data.getSummoned()) {
                    // making sure effects up to date
                    PetOwner petOwner = data.getPet(entity.getType());
                    data.updateEffect(petOwner.getPet(), petOwner.getLevel(), false);

                    Location loc = player.getLocation();
                    Location pLoc = entity.getLocation();

                    // making sure pet is nearby
                    //noinspection deprecation
                    if (player.isOnGround() && loc.getWorld().equals(pLoc.getWorld())) {
                        double distance = loc.distanceSquared(pLoc);

                        if (distance > 400)
                            entity.teleport(player);
                    }

                    // making sure pet is not dead lol
                    if (entity.isDead()) {
                        //eIterator.remove();
                        entity.remove();
                        //data.updateEffect(petOwner.getPet(), petOwner.getLevel(), true);

                        if (data.getSummoned().isEmpty()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }, 20, 20);
    }

    public Entity spawnPet(EntityType type, Player owner) {
        Location loc = owner.getLocation();
        LivingEntity entity = (LivingEntity) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, type);
        PlayerManager playerManager = plugin.getPlayerManager();
        PlayerData data = playerManager.getPlayerData(owner.getUniqueId());
        PetOwner petOwner = data.getPet(entity.getType());
        int level = petOwner.getPet().getLevel();
        PetManager petManager = plugin.getPetManager();

        String name = StringUtils.capEntityName(type);

        entity.setCustomName(StringUtils.toColor("&a" + name + " Pet "));
        entity.setCustomNameVisible(true);
        entity.setInvulnerable(true);

        EntityLiving entityLiving = ((CraftLivingEntity) entity).getHandle();
        if (entityLiving instanceof EntityInsentient entityInsentient) {
            entityInsentient.bP.a(0, new PathfinderPlayer(entityInsentient, owner, 2.0F));
        } else {
            throw new IllegalArgumentException(entityLiving.getClass().getSimpleName() + " is not an instance of an EntityInsentient.");
        }

        return entity;
    }
}
