package me.aqua.fadepets.player;

import java.util.*;

public class PlayerManager {

    private final Map<UUID, PlayerData> playerManager = new HashMap<>();
    private final Set<PlayerData> online = new HashSet<>();

    public PlayerData getPlayerData(UUID uuid) {
        playerManager.putIfAbsent(uuid, new PlayerData(uuid));
        return playerManager.get(uuid);
    }

    public void addPlayerData(UUID uuid, PlayerData data) {
        playerManager.put(uuid, data);
    }

    public boolean hasData(UUID uuid) {
        return playerManager.containsKey(uuid);
    }

    public void unSummonAllPets() {
        playerManager.keySet().forEach(uuid -> playerManager.get(uuid).removeAllSummons());
    }

    public Set<UUID> getAllPlayers() {
        return playerManager.keySet();
    }

    public void addOnlineData(UUID uuid) {
        online.add(playerManager.get(uuid));
    }

    public Set<PlayerData> getOnlineData() {
        return online;
    }

    public void removeOnline(PlayerData data) {
        online.remove(data);
    }
}
