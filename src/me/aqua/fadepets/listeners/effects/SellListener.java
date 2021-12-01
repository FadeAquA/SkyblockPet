package me.aqua.fadepets.listeners.effects;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.player.PlayerData;
import me.aqua.fadepets.player.PlayerManager;
import me.aqua.fadepets.utils.StringUtils;
import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.NumberFormat;

public record SellListener(PetsPlugin plugin) implements Listener {

    @EventHandler
    public void onEvent(ShopPreTransactionEvent event) {

        if (event.getShopAction() == ShopManager.ShopAction.SELL || event.getShopAction() == ShopManager.ShopAction.SELL_ALL) {
            double price = event.getPrice();
            Player player = event.getPlayer();
            PlayerManager manager = plugin.getPlayerManager();
            PlayerData data = manager.getPlayerData(player.getUniqueId());
            if (data.getMoneyBoost() > 0) {
                if (manager.hasData(player.getUniqueId()) && price > 0) {
                    double boost = data.getMoneyBoost() / 100.0;
                    double boostAmount = price * boost;
                    plugin.getEco().depositPlayer(player, boostAmount);
                    String format = NumberFormat.getInstance().format(boostAmount);
                    player.sendMessage(StringUtils.toColor("&a&l+ $" + format + " &7(Pet Boost)"));
                }
            }
        }
    }
}
