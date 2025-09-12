package com.ben.sd.commands;

import com.ben.sd.managers.BalanceManager;
import com.ben.sd.managers.TabManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SdAdminCommand implements CommandExecutor {
    private final BalanceManager balanceManager;
    private final TabManager tabManager;

    public SdAdminCommand(Object plugin, BalanceManager balanceManager, TabManager tabManager) {
        this.balanceManager = balanceManager;
        this.tabManager = tabManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("sd.admin")) {
            sender.sendMessage(Component.text("No permission."));
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(Component.text("Usage: /sd <add|set|remove> <player> <amount>"));
            return true;
        }

        String sub = args[0].toLowerCase();
        String targetName = args[1];
        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Amount must be a number."));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) target = Bukkit.getPlayerExact(targetName);
        if (target == null) target = Bukkit.getOfflinePlayer(targetName);
        UUID uuid = target.getUniqueId();

        switch (sub) {
            case "add" -> {
                balanceManager.addBalance(uuid, amount);
                balanceManager.save();
                notifyAndUpdate(target);
                sender.sendMessage(Component.text("Added $" + amount + " to " + target.getName() + "."));
            }
            case "set" -> {
                balanceManager.setBalance(uuid, amount);
                balanceManager.save();
                notifyAndUpdate(target);
                sender.sendMessage(Component.text("Set " + target.getName() + " balance to $" + amount + "."));
            }
            case "remove" -> {
                balanceManager.addBalance(uuid, -amount);
                balanceManager.save();
                notifyAndUpdate(target);
                sender.sendMessage(Component.text("Removed $" + amount + " from " + target.getName() + "."));
            }
            default -> sender.sendMessage(Component.text("Usage: /sd <add|set|remove> <player> <amount>"));
        }
        return true;
    }

    private void notifyAndUpdate(OfflinePlayer target) {
        if (target.isOnline()) {
            Player p = target.getPlayer();
            tabManager.updateTabFor(p);
            p.sendMessage(Component.text("Your balance is now updated."));
        }
    }
}
