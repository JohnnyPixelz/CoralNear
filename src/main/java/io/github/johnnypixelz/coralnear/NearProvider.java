package io.github.johnnypixelz.coralnear;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NearProvider implements InventoryProvider {
    private int iterations;
    private boolean lock;

    public static void open(Player player) {
        SmartInventory.builder()
                .title(CoralNear.color(CoralNear.getInstance().getConfig().getString("menu.title")))
                .size(3, 9)
                .provider(new NearProvider())
                .manager(CoralNear.getInventoryManager())
                .build()
                .open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        refresh(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if (iterations++ < CoralNear.getInstance().getConfig().getInt("update-delay")) return;
        iterations = 0;

        refresh(player, contents);
    }

    private void refresh(Player player, InventoryContents contents) {
        if (lock) return;

        HashMap<Player, Integer> nearbyPlayers = new HashMap<>();

        lock = true;
        Bukkit.getScheduler().runTaskAsynchronously(CoralNear.getInstance(), () -> {
            int i = 0;
            for (Player near : Bukkit.getOnlinePlayers()) {
                if (i == 27) break;
                if (player == near) continue;
                if (player.getWorld() != near.getWorld()) continue;

                nearbyPlayers.put(near, (int) near.getLocation().distance(player.getLocation()));

                i++;
            }

           Bukkit.getScheduler().runTask(CoralNear.getInstance(), () -> {
               contents.fill(null);
               nearbyPlayers.entrySet()
                       .stream()
                       .sorted(Map.Entry.comparingByValue())
                       .forEach(entry -> {
                           ItemStack head;

                           try {
                               head = new ItemStack(Material.PLAYER_HEAD);
                           } catch (NoSuchFieldError ex) {
                               head = new ItemStack(Objects.requireNonNull(Material.getMaterial("SKULL_ITEM")), 1, (short) 3);
                           }

                           SkullMeta meta = (SkullMeta) head.getItemMeta();
                           meta.setOwner(entry.getKey().getName());
                           meta.setDisplayName(CoralNear.color(CoralNear.getInstance().getConfig().getString("menu.head").replace("%name%", entry.getKey().getName()).replace("%distance%", String.valueOf(entry.getValue()))));
                           head.setItemMeta(meta);

                           contents.add(ClickableItem.empty(head));
                       });
               lock = false;
           });
        });

    }
}
