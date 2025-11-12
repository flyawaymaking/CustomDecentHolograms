package com.flyaway.cdh.managers;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private final CustomDecentHolograms plugin;
    private final File dataFolder;

    public PlayerDataManager(CustomDecentHolograms plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public PlayerData getPlayerData(Player player) {
        return loadPlayerData(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        return loadPlayerData(uuid);
    }

    private PlayerData loadPlayerData(UUID uuid) {
        File playerFile = new File(dataFolder, uuid.toString() + ".yml");

        if (!playerFile.exists()) {
            return new PlayerData(uuid);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        PlayerData data = new PlayerData(uuid);

        // Загрузка только имен голограмм
        if (config.contains("holograms")) {
            for (String hologramName : config.getConfigurationSection("holograms").getKeys(false)) {
                data.addHologram(hologramName);
            }
        }

        return data;
    }

    /**
     * Добавляет голограмму для игрока
     */
    public boolean addHologram(Player player, String hologramName) {
        return addHologram(player.getUniqueId(), hologramName);
    }

    /**
     * Добавляет голограмму для игрока по UUID
     */
    public boolean addHologram(UUID uuid, String hologramName) {
        try {
            PlayerData data = loadPlayerData(uuid);
            data.addHologram(hologramName);
            savePlayerData(data);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при добавлении голограммы для игрока " + uuid + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет голограмму у игрока
     */
    public boolean deleteHologram(Player player, String hologramName) {
        return deleteHologram(player.getUniqueId(), hologramName);
    }

    /**
     * Удаляет голограмму у игрока по UUID
     */
    public boolean deleteHologram(UUID uuid, String hologramName) {
        try {
            PlayerData data = loadPlayerData(uuid);
            boolean removed = data.removeHologram(hologramName);
            if (removed) {
                savePlayerData(data);
            }
            return removed;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при удалении голограммы у игрока " + uuid + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, есть ли у игрока голограмма с указанным именем
     */
    public boolean hasHologram(Player player, String hologramName) {
        PlayerData data = loadPlayerData(player.getUniqueId());
        return data.hasHologram(hologramName);
    }

    /**
     * Получает количество голограмм у игрока
     */
    public int getHologramCount(Player player) {
        return getHologramCount(player.getUniqueId());
    }

    /**
     * Получает количество голограмм у игрока по UUID
     */
    public int getHologramCount(UUID uuid) {
        PlayerData data = loadPlayerData(uuid);
        return data.getHologramCount();
    }

    private void savePlayerData(PlayerData data) {
        File playerFile = new File(dataFolder, data.getPlayerUUID().toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();

        // Сохранение только имен голограмм
        for (String hologramName : data.getHologramNames()) {
            config.set("holograms." + hologramName, true);
        }

        try {
            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить данные игрока " + data.getPlayerUUID() + ": " + e.getMessage());
        }
    }

    public void deletePlayerData(UUID uuid) {
        File playerFile = new File(dataFolder, uuid.toString() + ".yml");
        if (playerFile.exists()) {
            playerFile.delete();
        }
    }
}
