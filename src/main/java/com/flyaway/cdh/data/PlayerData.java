package com.flyaway.cdh.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private final UUID playerUUID;
    private final Set<String> hologramNames;

    public PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.hologramNames = new HashSet<>();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void addHologram(String name) {
        hologramNames.add(name.toLowerCase());
    }

    public boolean removeHologram(String name) {
        return hologramNames.remove(name.toLowerCase());
    }

    public Set<String> getHologramNames() {
        return new HashSet<>(hologramNames);
    }

    public int getHologramCount() {
        return hologramNames.size();
    }

    public boolean hasHologram(String name) {
        return hologramNames.contains(name.toLowerCase());
    }
}
