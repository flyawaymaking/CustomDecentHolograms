package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public class HideCommand extends SubCommand {

    public HideCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 1);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        if (args.length == 1) {
            // Переключение видимости всех голограмм игрока
            toggleAllHolograms(player);
        } else if (args.length == 2) {
            // Переключение видимости конкретной голограммы
            String hologramName = args[1];
            toggleSingleHologram(player, hologramName);
        } else {
            plugin.getMessageManager().sendMessage(player, "invalid-arguments");
        }

        return true;
    }

    private void toggleAllHolograms(Player player) {
        Set<String> hologramNames = plugin.getHologramManager().getPlayerHologramNames(player);

        if (hologramNames.isEmpty()) {
            plugin.getMessageManager().sendMessage(player, "no-holograms");
            return;
        }

        boolean allHidden = plugin.getHologramManager().toggleAllHolograms(player, hologramNames);

        String messageKey = allHidden ? "all-holograms-shown" : "all-holograms-hidden";
        plugin.getMessageManager().sendMessage(player, messageKey);
    }

    private void toggleSingleHologram(Player player, String hologramName) {
        if (!plugin.getPlayerDataManager().hasHologram(player, hologramName)) {
            plugin.getMessageManager().sendMessage(player, "hologram-not-found");
            return;
        }

        boolean newVisibility = plugin.getHologramManager().toggleHologram(player, hologramName);

        String messageKey = newVisibility ? "hologram-shown" : "hologram-hidden";
        plugin.getMessageManager().sendMessage(player, messageKey,
                Map.of("hologram", hologramName));
    }
}
