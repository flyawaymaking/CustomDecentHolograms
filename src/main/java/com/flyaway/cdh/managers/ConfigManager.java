package com.flyaway.cdh.managers;

import com.flyaway.cdh.CustomDecentHolograms;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final CustomDecentHolograms plugin;
    private FileConfiguration config;

    public ConfigManager(CustomDecentHolograms plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    // Геттеры
    public String getPrefix() {
        return config.getString("prefix", "<gray>[<green>CDH</green>]</gray>");
    }

    public boolean isWorldGuardEnabled() {
        return config.getBoolean("worldguard.enabled", false);
    }

    public int getMaxLines() {
        return config.getInt("limits.max-lines", 6);
    }

    public int getMaxLineLength() {
        return config.getInt("limits.max-line-length", 40);
    }

    public java.util.List<String> getHologramDefaultLines() {
        return config.getStringList("default-lines");
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "<red>Сообщение не найдено: " + key);
    }

    public List<String> getMessageList(String key) {
        return config.getStringList("messages." + key);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
