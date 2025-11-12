package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(CustomDecentHolograms plugin) {
        super(plugin, "cdh.reload", 1);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        plugin.reload();
        plugin.getMessageManager().sendMessage(player, "reload-success");
        return true;
    }
}
