package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.utils.PermissionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateCommand extends SubCommand {

    public CreateCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 2);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];

        // Проверка лимита голограмм
        int hologramLimit = PermissionUtils.getHologramLimit(player);
        int currentHolograms = plugin.getHologramManager().getPlayerHologramCount(player);

        if (currentHolograms >= hologramLimit && !PermissionUtils.canBypassLimits(player)) {
            plugin.getMessageManager().sendMessage(player, "hologram-limit-reached",
                    Map.of("limit", String.valueOf(hologramLimit)));
            return true;
        }

        // Проверка WorldGuard
        if (!plugin.getWorldGuardManager().canBuildHere(player, player.getLocation())) {
            plugin.getMessageManager().sendMessage(player, "worldguard-deny");
            return true;
        }

        // Проверка существования голограммы
        if (plugin.getPlayerDataManager().hasHologram(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-already-exists",
                    Map.of("hologram", hologramName));
            return true;
        }

        // Получаем строки по умолчанию из конфига
        List<String> defaultLines = plugin.getConfigManager().getHologramDefaultLines();
        List<String> processedLines = new ArrayList<>();

        // Заменяем плейсхолдеры в строках
        for (String line : defaultLines) {
            String processedLine = line.replace("{player}", player.getName()).replace("{hologram}", hologramName);
            processedLines.add(processedLine);
        }

        // Создаем голограмму
        Location location = player.getLocation();
        location.setY(location.getY() + 2);
        boolean success = plugin.getHologramManager().createHologram(player, hologramName, processedLines, location);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "hologram-created",
                    Map.of("hologram", hologramName));
        } else {
            plugin.getMessageManager().sendMessage(player, "hologram-creation-failed",
                    Map.of("hologram", hologramName));
        }

        return true;
    }
}
