package com.flyaway.cdh;

import com.flyaway.cdh.commands.HologramCommand;
import com.flyaway.cdh.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CustomDecentHolograms extends JavaPlugin {

    private static CustomDecentHolograms instance;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private HologramManager hologramManager;
    private WorldGuardManager worldGuardManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        // Инициализация менеджеров
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(configManager);
        this.worldGuardManager = new WorldGuardManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.hologramManager = new HologramManager(this);

        // Загрузка конфигураций
        configManager.loadConfig();

        // Регистрация команд
        registerCommands();

        getLogger().info("CustomDecentHolograms успешно включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomDecentHolograms успешно выключен!");
    }

    public void reload() {
        configManager.reloadConfig();
        worldGuardManager.reload();
        getLogger().info("Конфигурация перезагружена!");
    }

    private void registerCommands() {
        HologramCommand hologramCommand = new HologramCommand(this);
        Objects.requireNonNull(getCommand("hg")).setExecutor(hologramCommand);
        Objects.requireNonNull(getCommand("hg")).setTabCompleter(hologramCommand);
    }

    // Геттеры
    public static CustomDecentHolograms getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
