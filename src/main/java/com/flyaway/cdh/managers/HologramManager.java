package com.flyaway.cdh.managers;

import com.flyaway.cdh.CustomDecentHolograms;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class HologramManager {

    private final CustomDecentHolograms plugin;

    public HologramManager(CustomDecentHolograms plugin) {
        this.plugin = plugin;
    }

    public boolean createHologram(Player player, String name, List<String> lines, Location location) {
        try {
            String hologramId = "cdh_" + player.getUniqueId() + "_" + name;

            // Создание через DHAPI
            Hologram hologram = DHAPI.createHologram(hologramId, location, true, lines);
            if (hologram == null) {
                plugin.getLogger().warning("Не удалось создать голограмму " + hologramId);
                return false;
            }

            plugin.getPlayerDataManager().addHologram(player, name);
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при создании голограммы: " + e.getMessage());
            return false;
        }
    }

    public boolean moveHologram(Player player, String name, Location newLocation) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return false;
            }

            DHAPI.moveHologram(hologram, newLocation);

            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при перемещении голограммы: " + e.getMessage());
            return false;
        }
    }

    public Hologram getHologram(Player player, String name) {
        String hologramId = "cdh_" + player.getUniqueId() + "_" + name;
        return DHAPI.getHologram(hologramId);
    }

    public boolean deleteHologram(Player player, String name) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram != null) {
                DHAPI.removeHologram(hologram.getName());
            }

            // Удаление записи из данных игрока
            return plugin.getPlayerDataManager().deleteHologram(player, name);
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при удалении голограммы: " + e.getMessage());
            return false;
        }
    }

    public boolean editHologramLine(Player player, String name, int lineIndex, String text) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return false;
            }

            if (lineIndex < 0 || lineIndex >= hologram.getPage(0).size()) {
                return false;
            }

            DHAPI.setHologramLine(hologram, lineIndex, text);

            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при редактировании линии голограммы: " + e.getMessage());
            return false;
        }
    }

    public boolean addHologramLine(Player player, String name, String text) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return false;
            }

            DHAPI.addHologramLine(hologram, text);

            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при добавлении линии голограммы: " + e.getMessage());
            return false;
        }
    }

    public boolean removeHologramLine(Player player, String name, int lineIndex) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return false;
            }

            if (lineIndex < 0 || lineIndex >= hologram.getPage(0).size()) {
                return false;
            }

            DHAPI.removeHologramLine(hologram, lineIndex);

            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при удалении линии голограммы: " + e.getMessage());
            return false;
        }
    }

    public boolean toggleAllHolograms(Player player, Set<String> names) {
        boolean anyVisible = false;

        // Проверяем, есть ли хоть одна видимая голограмма
        for (String hologramName : names) {
            Hologram hologram = getHologram(player, hologramName);
            if (hologram != null && hologram.isVisibleState()) {
                anyVisible = true;
                break;
            }
        }

        // Если есть хотя бы одна видимая — скрываем все, иначе показываем все
        boolean makeVisible = !anyVisible;

        for (String hologramName : names) {
            Hologram hologram = getHologram(player, hologramName);
            if (hologram == null) continue;

            hologram.setDefaultVisibleState(makeVisible);
            if (makeVisible) {
                hologram.showAll();
            } else {
                hologram.hideAll();
            }
        }

        return makeVisible;
    }

    public boolean toggleHologram(Player player, String name) {
        Hologram hologram = getHologram(player, name);
        if (hologram == null) return false;

        boolean currentlyVisible = hologram.isVisibleState();
        boolean newVisibility = !currentlyVisible;

        hologram.setDefaultVisibleState(newVisibility);
        if (newVisibility) {
            hologram.showAll();
        } else {
            hologram.hideAll();
        }

        return newVisibility;
    }

    public List<String> getHologramLines(Player player, String name) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return List.of();
            }

            return hologram.getPage(0).getLines().stream()
                    .map(line -> line.getText() != null ? line.getText() : "")
                    .toList();

        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при получении строк голограммы: " + e.getMessage());
            return List.of();
        }
    }

    public int countHologramLines(Player player, String name) {
        try {
            Hologram hologram = getHologram(player, name);
            if (hologram == null) {
                return 0;
            }

            return hologram.getPage(0).size();
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при получении линий голограммы: " + e.getMessage());
            return 0;
        }
    }

    public boolean hologramExists(Player player, String name) {
        return getHologram(player, name) != null;
    }

    public int getPlayerHologramCount(Player player) {
        return plugin.getPlayerDataManager().getHologramCount(player);
    }

    public Set<String> getPlayerHologramNames(Player player) {
        return plugin.getPlayerDataManager().getPlayerData(player).getHologramNames();
    }
}
