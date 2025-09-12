package com.ben.sd.managers;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BalanceManager {
    private final Plugin plugin;
    private final File file;
    private final FileConfiguration data;

    private final Map<UUID, Double> balances = new HashMap<>();
    private final Map<UUID, Boolean> hidden = new HashMap<>();

    public BalanceManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "balances.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try { file.createNewFile(); } catch (IOException ignored) {}
        }
        this.data = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void load() {
        if (data.getConfigurationSection("players") == null) return;
        for (String key : data.getConfigurationSection("players").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double bal = data.getDouble("players." + key + ".balance", 0.0);
                boolean hid = data.getBoolean("players." + key + ".hidden", false);
                balances.put(uuid, bal);
                hidden.put(uuid, hid);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void save() {
        for (Map.Entry<UUID, Double> e : balances.entrySet()) {
            String base = "players." + e.getKey();
            data.set(base + ".balance", e.getValue());
            data.set(base + ".hidden", hidden.getOrDefault(e.getKey(), false));
        }
        try {
            data.save(file);
        } catch (IOException ex) {
            plugin.getLogger().warning("Failed to save balances.yml: " + ex.getMessage());
        }
    }

    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, 0.0);
    }

    public void setBalance(UUID uuid, double amount) {
        balances.put(uuid, Math.max(0.0, amount));
    }

    public void addBalance(UUID uuid, double amount) {
        if (amount == 0) return;
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean isHidden(UUID uuid) {
        return hidden.getOrDefault(uuid, false);
    }

    public void setHidden(UUID uuid, boolean hide) {
        hidden.put(uuid, hide);
    }
}
