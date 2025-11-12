package com.flyaway.cdh.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class MessageManager {

    private final ConfigManager configManager;
    private final MiniMessage miniMessage;

    public MessageManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.miniMessage = MiniMessage.miniMessage();
    }

    public void sendMessage(Player player, String messageKey) {
        sendMessage(player, messageKey, Map.of());
    }

    public void sendMessage(Player player, String messageKey, Map<String, String> placeholders) {
        String message = configManager.getMessage(messageKey);
        player.sendMessage(getFormattedLine(player, message, placeholders));
    }

    public void sendMessageList(Player player, String messageListKey) {
        List<String> messages = configManager.getMessageList(messageListKey);
        sendMessageList(player, messages, Map.of());
    }

    public void sendMessageList(Player player, List<String> messages, Map<String, String> placeholders) {
        if (messages == null || messages.isEmpty()) return;

        // Объединяем все сообщения в один компонент с переносами строк
        Component combinedMessage = buildCombinedMessage(messages, placeholders);
        player.sendMessage(combinedMessage);
    }

    public String getRawMessage(String messageKey, Map<String, String> placeholders) {
        return processPlaceholders(configManager.getMessage(messageKey), placeholders);
    }

    private Component getFormattedLine(Player player, String line, Map<String, String> placeholders) {
        String prefix = configManager.getPrefix();
        String fullMessage = prefix + " " + line;

        return miniMessage.deserialize(processPlaceholders(fullMessage, placeholders));
    }

    private String processPlaceholders(String message, Map<String, String> placeholders) {
        if (placeholders == null || placeholders.isEmpty()) {
            return message;
        }

        String result = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private Component buildCombinedMessage(List<String> messages, Map<String, String> placeholders) {
        Component result = Component.empty();
        String prefix = configManager.getPrefix();

        for (int i = 0; i < messages.size(); i++) {
            String message = messages.get(i);

            // Обрабатываем плейсхолдеры
            String processedMessage = processPlaceholders(message, placeholders);

            // Добавляем префикс только к первой строке
            String fullMessage = (i == 0) ? prefix + " " + processedMessage : processedMessage;

            // Парсим MiniMessage
            Component lineComponent = miniMessage.deserialize(fullMessage);

            // Добавляем к результату
            if (i > 0) {
                result = result.append(Component.newline());
            }
            result = result.append(lineComponent);
        }

        return result;
    }
}
