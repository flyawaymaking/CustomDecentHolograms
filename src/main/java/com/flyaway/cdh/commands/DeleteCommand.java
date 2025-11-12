package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

import java.util.Map;

public class DeleteCommand extends SubCommand {

    public DeleteCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 2);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];

        // Проверка существования голограммы и владения
        if (!plugin.getPlayerDataManager().hasHologram(player, hologramName)
                && !plugin.getHologramManager().hologramExists(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-not-found");
            return true;
        }

        // Удаляем голограмму
        boolean success = plugin.getHologramManager().deleteHologram(player, hologramName);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "hologram-deleted",
                    Map.of("hologram", hologramName));
        } else {
            plugin.getMessageManager().sendMessage(player, "hologram-delete-failed",
                    Map.of("hologram", hologramName));
        }

        return true;
    }
}
