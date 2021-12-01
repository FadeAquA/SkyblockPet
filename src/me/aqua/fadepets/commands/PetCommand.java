package me.aqua.fadepets.commands;

import me.aqua.fadepets.PetsPlugin;
import me.aqua.fadepets.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record PetCommand(PetsPlugin plugin) implements CommandExecutor {

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
        if (args.length == 1 && args[0].equals("unsummon")) {
        plugin.getPlayerManager().getPlayerData(player.getUniqueId()).removeAllSummons();
        player.sendMessage(StringUtils.toColor("&a&l[!] &aYou unsummoned all your pets!"));
        return true;
        }

        plugin.getPetMenu().open(player);
        return true;
        }

        return false;
        }

}
