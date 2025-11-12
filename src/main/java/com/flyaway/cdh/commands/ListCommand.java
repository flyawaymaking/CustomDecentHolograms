package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListCommand extends SubCommand {

    public ListCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 1);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        Set<String> hologramNames = plugin.getHologramManager().getPlayerHologramNames(player);
        int hologramCount = hologramNames.size();

        if (hologramCount == 0) {
            plugin.getMessageManager().sendMessage(player, "no-holograms");
            return true;
        }
        List<String> messages = new ArrayList<>();

        Map<String, String> headerPlaceholders = Map.of("count", String.valueOf(hologramCount));
        messages.add(plugin.getMessageManager().getRawMessage("list-header", headerPlaceholders));

        // Отображаем простой список голограмм
        int index = 1;
        for (String hologramName : hologramNames) {
            Map<String, String> itemPlaceholders = Map.of(
                    "index", String.valueOf(index),
                    "name", hologramName
            );
            messages.add(plugin.getMessageManager().getRawMessage("list-item", itemPlaceholders));
            index++;
        }

        plugin.getMessageManager().sendMessageList(player, messages, Map.of());
        return true;
    }
}
