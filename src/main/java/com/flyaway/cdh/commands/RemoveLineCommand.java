package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

import java.util.Map;

public class RemoveLineCommand extends SubCommand {

    public RemoveLineCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 3);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];
        String lineIndexStr = args[2];

        // Проверка существования голограммы и владения
        if (!plugin.getPlayerDataManager().hasHologram(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-not-found");
            return true;
        }

        // Проверка индекса строки
        int lineIndex;
        try {
            lineIndex = Integer.parseInt(lineIndexStr) - 1; // Переводим в 0-based индекс
        } catch (NumberFormatException e) {
            plugin.getMessageManager().sendMessage(player, "invalid-line-number");
            return true;
        }

        // Проверка существования строки
        int lineCount = plugin.getHologramManager().countHologramLines(player, hologramName);
        if (lineIndex < 0 || lineIndex >= lineCount) {
            plugin.getMessageManager().sendMessage(player, "line-not-found");
            return true;
        }

        // Удаляем строку
        boolean success = plugin.getHologramManager().removeHologramLine(player, hologramName, lineIndex);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "line-removed",
                    Map.of("hologram", hologramName, "line", lineIndexStr));
        } else {
            plugin.getMessageManager().sendMessage(player, "line-remove-failed",
                    Map.of("hologram", hologramName, "line", lineIndexStr));
        }

        return true;
    }
}
