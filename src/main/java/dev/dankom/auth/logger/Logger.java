package dev.dankom.auth.logger;

import dev.dankom.auth.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    public static void log(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.PREFIX + " " + msg));
    }
}
