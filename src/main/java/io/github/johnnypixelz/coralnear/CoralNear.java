package io.github.johnnypixelz.coralnear;

import fr.minuskube.inv.InventoryManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CoralNear extends JavaPlugin {
    private static InventoryManager inventoryManager;
    private static CoralNear instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        getCommand("near").setExecutor(new NearCommand());
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static CoralNear getInstance() {
        return instance;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
