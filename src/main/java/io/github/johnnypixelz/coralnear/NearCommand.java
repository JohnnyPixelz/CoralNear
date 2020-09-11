package io.github.johnnypixelz.coralnear;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(CoralNear.color(CoralNear.getInstance().getConfig().getString("messages.console")));
            return true;
        }

        Player player = (Player) commandSender;

        Sound chestOpen;

        try {
            chestOpen = Sound.valueOf("BLOCK_CHEST_OPEN");
        } catch (IllegalArgumentException ex) {
            chestOpen = Sound.valueOf("CHEST_OPEN");
        }

        player.playSound(player.getLocation(), chestOpen, 1, 1);
        NearProvider.open(player);
        return true;
    }
}
