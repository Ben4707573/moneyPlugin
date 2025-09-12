package com.ben.sd;

import com.ben.sd.commands.HideBalanceCommand;
import com.ben.sd.commands.PayCommand;
import com.ben.sd.commands.SdAdminCommand;
import com.ben.sd.managers.BalanceManager;
import com.ben.sd.managers.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerDollarsPlugin extends JavaPlugin implements Listener {

    private BalanceManager balanceManager;
    private TabManager tabManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.balanceManager = new BalanceManager(this);
        this.tabManager = new TabManager(this, balanceManager);

        getServer().getPluginManager().registerEvents(this, this);

        // Commands
        getCommand("sd").setExecutor(new SdAdminCommand(this, balanceManager, tabManager));
        getCommand("pay").setExecutor(new PayCommand(this, balanceManager, tabManager));
        getCommand("hidebalance").setExecutor(new HideBalanceCommand(this, balanceManager, tabManager));

        // Update current online players (restarts/reloads)
        for (Player p : Bukkit.getOnlinePlayers()) {
            tabManager.updateTabFor(p);
        }
    }

    @Override
    public void onDisable() {
        if (balanceManager != null) {
            balanceManager.save();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        tabManager.updateTabFor(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // optional: save on quit
        balanceManager.save();
    }
}
