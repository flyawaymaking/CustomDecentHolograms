package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.utils.PermissionUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.Map;

public class EditLineCommand extends SubCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public EditLineCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 4);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        String hologramName = args[1];
        String lineIndexStr = args[2];
        StringBuilder textBuilder = new StringBuilder();

        for (int i = 3; i < args.length; i++) {
            textBuilder.append(args[i]).append(" ");
        }
        String text = textBuilder.toString().trim();

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

        // Валидация MiniMessages текста
        try {
            miniMessage.deserialize(text);
        } catch (Exception e) {
            plugin.getMessageManager().sendMessage(player, "invalid-format");
            return true;
        }

        // Редактируем строку
        boolean success = plugin.getHologramManager().editHologramLine(player, hologramName, lineIndex, text);

        if (success) {
            plugin.getMessageManager().sendMessage(player, "line-edited",
                    Map.of("hologram", hologramName, "line", lineIndexStr));
        } else {
            plugin.getMessageManager().sendMessage(player, "line-edit-failed",
                    Map.of("hologram", hologramName, "line", lineIndexStr));
        }

        return true;
    }
}
