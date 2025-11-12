package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.entity.Player;

public class FormatsCommand extends SubCommand {

    public FormatsCommand(CustomDecentHolograms plugin) {
        super(plugin, null, 1);
    }

    @Override
    protected boolean handleCommand(Player player, String[] args) {
        plugin.getMessageManager().sendMessageList(player, "formats-list");
        return true;
    }
}
