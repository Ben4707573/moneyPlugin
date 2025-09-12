package com.ben.sd.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;

public class TabManager {
    private final Plugin plugin;
    private final BalanceManager balanceManager;
    private final DecimalFormat fmt = new DecimalFormat("#,##0.00");

    public TabManager(Plugin plugin, BalanceManager balanceManager) {
        this.plugin = plugin;
        this.balanceManager = balanceManager;
    }

    public void updateTabFor(Player player) {
        double bal = balanceManager.getBalance(player.getUniqueId());
        String money = "$" + fmt.format(bal) + " SD";
        Component header = Component.text("Your Balance: " + money);
        // Only show self balance; do not show others here to respect privacy concept
        player.sendPlayerListHeader(header);
        // Clear footer
        player.sendPlayerListFooter(Component.empty());
    }
}
