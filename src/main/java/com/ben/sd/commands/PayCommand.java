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

public class PayCommand implements CommandExecutor {
    private final BalanceManager balanceManager;
    private final TabManager tabManager;

    public PayCommand(Object plugin, BalanceManager balanceManager, TabManager tabManager) {
        this.balanceManager = balanceManager;
        this.tabManager = tabManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this."));
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(Component.text("Usage: /pay <player> <amount>"));
            return true;
        }

        String targetName = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Amount must be a number."));
            return true;
        }
        if (amount <= 0) {
            player.sendMessage(Component.text("Amount must be positive."));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) target = Bukkit.getPlayerExact(targetName);
        if (target == null) target = Bukkit.getOfflinePlayer(targetName);

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You can't pay yourself."));
            return true;
        }

        UUID from = player.getUniqueId();
        UUID to = target.getUniqueId();

        double bal = balanceManager.getBalance(from);
        if (bal < amount) {
            player.sendMessage(Component.text("Insufficient funds."));
            return true;
        }

    balanceManager.addBalance(from, -amount);
    balanceManager.addBalance(to, amount);
    balanceManager.save();

        tabManager.updateTabFor(player);
        if (target.isOnline()) tabManager.updateTabFor(target.getPlayer());

        player.sendMessage(Component.text("Paid $" + amount + " to " + target.getName() + "."));
        if (target.isOnline()) target.getPlayer().sendMessage(Component.text("You received $" + amount + " from " + player.getName() + "."));

        return true;
    }
}
