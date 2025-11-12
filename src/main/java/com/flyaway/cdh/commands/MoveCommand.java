package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class MoveCommand extends SubCommand {

    public MoveCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 2);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];

        // Проверка существования голограммы и владения
        if (!plugin.getPlayerDataManager().hasHologram(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-not-found");
            return true;
        }

        // Проверка WorldGuard
        if (!plugin.getWorldGuardManager().canBuildHere(player, player.getLocation())) {
            plugin.getMessageManager().sendMessage(player, "worldguard-deny");
            return true;
        }

        // Перемещаем голограмму
        Location location = player.getLocation();
        location.setY(location.getY() + 2);
        boolean success = plugin.getHologramManager().moveHologram(player, hologramName, location);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "hologram-moved",
                    Map.of("hologram", hologramName));
        } else {
            plugin.getMessageManager().sendMessage(player, "hologram-move-failed",
                    Map.of("hologram", hologramName));
        }

        return true;
    }
}
