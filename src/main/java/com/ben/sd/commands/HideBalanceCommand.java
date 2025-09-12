package com.ben.sd.commands;

import com.ben.sd.managers.BalanceManager;
import com.ben.sd.managers.TabManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HideBalanceCommand implements CommandExecutor {
    private final BalanceManager balanceManager;
    private final TabManager tabManager;

    public HideBalanceCommand(Object plugin, BalanceManager balanceManager, TabManager tabManager) {
        this.balanceManager = balanceManager;
        this.tabManager = tabManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this."));
            return true;
        }
        boolean newVal = !balanceManager.isHidden(player.getUniqueId());
    balanceManager.setHidden(player.getUniqueId(), newVal);
    // save hidden state immediately
    balanceManager.save();
        player.sendMessage(Component.text(newVal ? "Your balance is now hidden." : "Your balance is now visible."));
        tabManager.updateTabFor(player);
        return true;
    }
}
