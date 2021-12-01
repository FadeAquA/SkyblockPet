package me.aqua.fadepets.player;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.pets.Pet;
import me.aqua.fadepets.pets.PetOwner;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStorage {

    private FileConfiguration config;
    private File file;

    private final PetsPlugin plugin;
    private final PlayerManager manager;

    public FileStorage(PetsPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getPlayerManager();

        setup();
        load();
    }

    public void savePlayers() {
        manager.getAllPlayers().forEach(uuid -> {
            PlayerData data = manager.getPlayerData(uuid);

            for (EntityType type : data.getAllOwnedPets()) {
                String pet = type.toString();
                int level = data.getPet(type).getLevel();
                int upgrades = data.getPet(type).getUpgrades();

                config.set(uuid + "." + pet + ".upgrades", upgrades);
                config.set(uuid + "." + pet + ".level", level);
            }
        });

        try { config.save(file); }
        catch (IOException e) { Bukkit.getConsoleSender().sendMessage("&cCannot create a players.yml"); }
    }

    private void load() {
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);

            PlayerData data = new PlayerData(uuid);

            for (String type : config.getConfigurationSection(key).getKeys(false)) {
                EntityType entity = EntityType.valueOf(type);
                int level = config.getInt(key + "." + type + ".level");
                int upgrades = config.getInt(key + "." + type + ".upgrades");

                Pet pet = plugin.getPetManager().getPet(entity);
                data.addOwnership(entity, new PetOwner(level, pet, upgrades));
            }

            manager.addPlayerData(uuid, data);
        }
    }

    private void setup() {
        file = new File(plugin.getDataFolder(), "players.yml");

        try { file.createNewFile(); }
        catch(IOException e) { Bukkit.getConsoleSender().sendMessage("Error while creating players.yml for pets"); }

        config = YamlConfiguration.loadConfiguration(file);
    }
}
