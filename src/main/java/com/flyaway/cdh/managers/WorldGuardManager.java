package com.flyaway.cdh.managers;

import com.flyaway.cdh.CustomDecentHolograms;
import com.flyaway.cdh.utils.PermissionUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardManager {

    private final CustomDecentHolograms plugin;
    private boolean worldGuardEnabled = false;

    public WorldGuardManager(CustomDecentHolograms plugin) {
        this.plugin = plugin;
        checkWorldGuard();
    }

    private void checkWorldGuard() {
        try {
            if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                worldGuardEnabled = true;
                plugin.getLogger().info("WorldGuard интеграция включена");
            }
        } catch (Exception e) {
            worldGuardEnabled = false;
        }
    }

    public boolean canBuildHere(Player player, Location location) {
        if (!worldGuardEnabled || !plugin.getConfigManager().isWorldGuardEnabled()) {
            return true;
        }

        if (PermissionUtils.canBypassLimits(player)) {
            return true;
        }

        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));

            if (regions == null) {
                return true;
            }

            ApplicableRegionSet regionSet = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));

            // Проверка глобального региона
            ProtectedRegion globalRegion = regions.getRegion("__global__");
            if (globalRegion != null && regionSet.getRegions().contains(globalRegion)) {
                return true;
            }

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            // Проверка прав игрока в регионах
            for (ProtectedRegion region : regionSet) {
                if (region.isOwner(localPlayer) || region.isMember(localPlayer)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка при проверке WorldGuard: " + e.getMessage());
            return true;
        }
    }

    public boolean isWorldGuardEnabled() {
        return worldGuardEnabled && plugin.getConfigManager().isWorldGuardEnabled();
    }

    public void reload() {
        checkWorldGuard();
    }
}
