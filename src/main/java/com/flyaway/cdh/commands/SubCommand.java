package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected final CustomDecentHolograms plugin;
    private final String permission;
    private final int minArgs;

    public SubCommand(CustomDecentHolograms plugin, String permission, int minArgs) {
        this.plugin = plugin;
        this.permission = permission;
        this.minArgs = minArgs;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        if (!hasPermission(player)) {
            plugin.getMessageManager().sendMessage(player, "no-permission");
            return true;
        }

        if (args.length < minArgs) {
            plugin.getMessageManager().sendMessage(player, "invalid-arguments");
            return true;
        }

        return handleCommand(player, args);
    }

    protected boolean hasPermission(Player player) {
        return permission == null || player.hasPermission(permission);
    }

    protected abstract boolean handleCommand(Player player, String[] args);
}
