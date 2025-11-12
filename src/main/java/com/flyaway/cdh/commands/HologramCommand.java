package com.flyaway.cdh.commands;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramCommand implements CommandExecutor, TabExecutor {

    private final CustomDecentHolograms plugin;
    private final Map<String, SubCommand> subCommands;

    public HologramCommand(CustomDecentHolograms plugin) {
        this.plugin = plugin;
        this.subCommands = new HashMap<>();
        registerSubCommands();
    }

    private void registerSubCommands() {
        subCommands.put("create", new CreateCommand(plugin));
        subCommands.put("move", new MoveCommand(plugin));
        subCommands.put("edit", new EditLineCommand(plugin));
        subCommands.put("addline", new AddLineCommand(plugin));
        subCommands.put("removeline", new RemoveLineCommand(plugin));
        subCommands.put("delete", new DeleteCommand(plugin));
        subCommands.put("list", new ListCommand(plugin));
        subCommands.put("formats", new FormatsCommand(plugin));
        subCommands.put("hide", new HideCommand(plugin));
        subCommands.put("reload", new ReloadCommand(plugin));
        subCommands.put("help", new HelpCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        // Проверка основного права cdh.use
        if (!player.hasPermission("cdh.use")) {
            plugin.getMessageManager().sendMessage(player, "no-permission");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            showHelp(player);
            return true;
        }

        return subCommand.execute(sender, args);
    }

    private void showHelp(Player player) {
        plugin.getMessageManager().sendMessage(player, "help-message");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player player)) {
            return completions;
        }

        if (!player.hasPermission("cdh.use")) {
            return completions;
        }

        if (args.length == 1) {
            // автодополнение подкоманд
            for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
                String commandName = entry.getKey();
                if (commandName.equals("reload")) {
                    if (player.hasPermission("cdh.reload")) {
                        completions.add(commandName);
                    }
                } else {
                    completions.add(commandName);
                }
            }

        } else if (args.length == 2) {
            // имена голограмм
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("move") || subCommand.equals("edit") ||
                    subCommand.equals("addline") || subCommand.equals("removeline") ||
                    subCommand.equals("delete") || subCommand.equals("hide")) {

                completions.addAll(plugin.getHologramManager().getPlayerHologramNames(player));
            }

        } else if (args.length == 3) {
            // количество строк для edit и remove
            String subCommand = args[0].toLowerCase();
            String holoName = args[1];

            if (subCommand.equals("edit") || subCommand.equals("remove")) {
                if (plugin.getHologramManager().hologramExists(player, holoName)) {
                    int lines = plugin.getHologramManager().countHologramLines(player, holoName);
                    for (int i = 1; i <= lines; i++) {
                        completions.add(String.valueOf(i));
                    }
                }
            }

        } else if (args.length == 4) {
            // текущее значение строки для edit
            String subCommand = args[0].toLowerCase();
            String holoName = args[1];

            if (subCommand.equals("edit")) {
                try {
                    int lineIndex = Integer.parseInt(args[2]) - 1;
                    List<String> lines = plugin.getHologramManager().getHologramLines(player, holoName);
                    if (lineIndex >= 0 && lineIndex < lines.size()) {
                        completions.add(lines.get(lineIndex));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return completions;
    }
}
