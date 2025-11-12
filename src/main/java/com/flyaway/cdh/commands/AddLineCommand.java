package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.utils.PermissionUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.Map;

public class AddLineCommand extends SubCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public AddLineCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 3);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            textBuilder.append(args[i]).append(" ");
        }
        String text = textBuilder.toString().trim();

        // Проверка существования голограммы и владения
        if (!plugin.getPlayerDataManager().hasHologram(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-not-found");
            return true;
        }

        // Проверка лимита строк
        if (!PermissionUtils.canBypassLimits(player)) {
            int currentLines = plugin.getHologramManager().countHologramLines(player, hologramName);
            int maxLines = plugin.getConfigManager().getMaxLines();

            if (currentLines >= maxLines) {
                plugin.getMessageManager().sendMessage(player, "max-lines-reached",
                        Map.of("max", String.valueOf(maxLines), "current", String.valueOf(currentLines)));
                return true;
            }
        }

        // Проверка лимита символов (на сырой текст, без учета тегов MiniMessages)
        if (!PermissionUtils.canBypassLimits(player)) {
            int maxLength = plugin.getConfigManager().getMaxLineLength();
            // Проверяем длину исходного текста без форматирования
            String rawText = text.replaceAll("<[^>]*>", ""); // Удаляем теги MiniMessages
            if (rawText.length() > maxLength) {
                plugin.getMessageManager().sendMessage(player, "line-too-long",
                        Map.of("max", String.valueOf(maxLength), "current", String.valueOf(rawText.length())));
                return true;
            }
        }

        // Добавляем строку
        boolean success = plugin.getHologramManager().addHologramLine(player, hologramName, text);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "line-added",
                    Map.of("hologram", hologramName));
        } else {
            plugin.getMessageManager().sendMessage(player, "line-add-failed",
                    Map.of("hologram", hologramName));
        }

        return true;
    }
}
