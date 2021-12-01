package me.aqua.fadepets;

import me.aqua.fadepets.commands.PetCommand;
import me.aqua.fadepets.listeners.PlayerListener;
import me.aqua.fadepets.listeners.effects.ExpListener;
import me.aqua.fadepets.listeners.effects.SellListener;
import me.aqua.fadepets.listeners.menus.MenuListener;
import me.aqua.fadepets.listeners.menus.ShopListener;
import me.aqua.fadepets.listeners.menus.SummonListener;
import me.aqua.fadepets.listeners.menus.UpgradeListener;
import me.aqua.fadepets.menus.*;
import me.aqua.fadepets.pathfinder.LivingManager;
import me.aqua.fadepets.pets.PetManager;
import me.aqua.fadepets.player.FileStorage;
import me.aqua.fadepets.player.PlayerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PetsPlugin extends JavaPlugin {

    private PetManager petManager;
    private PlayerManager playerManager;
    private Economy eco;
    private static PetsPlugin instance;

    private PetMenu petMenu;
    private ShopMenu shopMenu;
    private SummonMenu summonMenu;
    private UpgradeMenu upgradeMenu;
    private ChanceMenu chanceMenu;

    private LivingManager livingManager;
    private FileStorage fileStorage;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        petManager = new PetManager(this);
        playerManager = new PlayerManager();
        fileStorage = new FileStorage(this);
        livingManager = new LivingManager(this);

        petMenu = new PetMenu();
        shopMenu = new ShopMenu(this);
        summonMenu = new SummonMenu(this);
        upgradeMenu = new UpgradeMenu(this);
        chanceMenu = new ChanceMenu(this);

        Objects.requireNonNull(getCommand("pet")).setExecutor(new PetCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ExpListener(this), this);
        getServer().getPluginManager().registerEvents(new SellListener(this), this);

        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(new ShopListener(this), this);
        getServer().getPluginManager().registerEvents(new SummonListener(this), this);
        getServer().getPluginManager().registerEvents(new UpgradeListener(this), this);
    }

    @Override
    public void onDisable() {
        playerManager.unSummonAllPets();
        fileStorage.savePlayers();
    }

    private boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economy = (RegisteredServiceProvider<Economy>)this.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economy != null) {
            this.eco = (Economy)economy.getProvider();
        }
        return this.eco != null;
    }

    public Economy getEco() {
        return eco;
    }

    public LivingManager getLivingManager() {
        return livingManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PetManager getPetManager() {
        return petManager;
    }

    public PetMenu getPetMenu() {
        return petMenu;
    }

    public ShopMenu getShopMenu() {
        return shopMenu;
    }

    public SummonMenu getSummonMenu() {
        return summonMenu;
    }

    public UpgradeMenu getUpgradeMenu() {
        return upgradeMenu;
    }

    public ChanceMenu getChanceMenu() {
        return chanceMenu;
    }

    public static PetsPlugin getInstance() {
        return instance;
    }
}
