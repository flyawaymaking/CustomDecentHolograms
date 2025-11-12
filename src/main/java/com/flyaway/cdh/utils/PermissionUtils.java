package com.flyaway.cdh.utils;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionUtils {

    public static int getHologramLimit(Player player) {
        int maxLimit = 0;

        // Получаем все права игрока и ищем cdh.amount.*
        for (PermissionAttachmentInfo permissionInfo : player.getEffectivePermissions()) {
            String permission = permissionInfo.getPermission();
            if (permission.startsWith("cdh.amount.")) {
                try {
                    int limit = Integer.parseInt(permission.substring("cdh.amount.".length()));
                    if (limit > maxLimit) {
                        maxLimit = limit;
                    }
                } catch (NumberFormatException e) {
                    // Игнорируем некорректные значения
                }
            }
        }

        return maxLimit;
    }

    public static boolean canBypassLimits(Player player) {
        return player.hasPermission("cdh.bypass");
    }
}
