package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.utils.PermissionUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class HelpCommand extends SubCommand {

    public HelpCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 1);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        // Отправляем список сообщений помощи
        plugin.getMessageManager().sendMessageList(player, "help-commands");

        // Информация о лимите
        Map<String, String> limitPlaceholders = Map.of("limit",
                String.valueOf(PermissionUtils.getHologramLimit(player)));
        plugin.getMessageManager().sendMessage(player, "help-limit", limitPlaceholders);

        return true;
    }
}
