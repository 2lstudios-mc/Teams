package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

class TeamsClaimCommand {
  TeamsClaimCommand(PluginManager pluginManager, CommandSender sender) {
    if (pluginManager.getPlugin("ProtectionWands") != null) {
      sender.sendMessage(ChatColor.RED + "Usa /claim para claimear un territorio!");
    } else {
      sender.sendMessage(ChatColor.RED + "Esta opcion aun no esta disponible!");
    }
  }
}
