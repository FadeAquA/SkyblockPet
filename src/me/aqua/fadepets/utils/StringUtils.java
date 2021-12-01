package me.aqua.fadepets.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public class StringUtils {

    public static String toColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String capEntityName(EntityType entityType) {
        String name = entityType.toString();
        return WordUtils.capitalizeFully(name.toLowerCase().replaceAll("_", " "));
    }
}
