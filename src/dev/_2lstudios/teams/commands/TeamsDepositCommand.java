package dev._2lstudios.teams.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsDepositCommand {
  TeamsDepositCommand(Economy economy, TeamPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender,
      String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (args.length > 1) {
        String senderName = sender.getName();
        TeamPlayer teamPlayer = tPlayerManager.getPlayer(senderName);
        Team team = teamManager.getTeam(teamPlayer.getTeam());
        if (team != null) {
          try {
            double amount = Float.parseFloat(args[1]);
            if (amount >= 1.0D) {
              if (economy.has((OfflinePlayer) player, amount)) {
                economy.withdrawPlayer((OfflinePlayer) player, amount);
                team.setMoney(team.getMoney() + amount);
                sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&aIngresaste &b$" + amount + " &aa tu team!"));
              } else {
                sender.sendMessage(ChatColor.RED + "No tienes dinero suficiente!");
              }
            } else {
              sender.sendMessage(ChatColor.RED + "El monto debe ser igual o mayor a 1!");
            }
          } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "No ingresaste un numero valido!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      } else {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/f deposit <cantidad>"));
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
    }
  }
}
