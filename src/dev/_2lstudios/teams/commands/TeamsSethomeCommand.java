package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.worldsentinel.WorldSentinel;
import dev._2lstudios.worldsentinel.region.Region;

class TeamsSethomeCommand {
  private boolean isRegionMember(PluginManager pluginManager, Player player, String senderName) {
    if (pluginManager.isPluginEnabled("WorldSentinel")) {
      Region region = WorldSentinel.getInstance().getRegionPlayerManager().getPlayer(player).getRegion();
      if (region != null && !region.getFlags().getCollection("owners").contains(senderName)
          && !region.getFlags().getCollection("members").contains(senderName))
        return false;
    }
    return true;
  }

  TeamsSethomeCommand(PluginManager pluginManager, TPlayerManager tPlayerManager, TeamManager teamManager,
      CommandSender sender, boolean homesEnabled) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!homesEnabled) {
        sender.sendMessage(ChatColor.RED + "Las homes estan desactivadas!");
      } else {
        String senderName = sender.getName();
        TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
        Team team = teamManager.getTeam(tPlayer.getTeam());
        if (team != null) {
          Role tPlayerRole = team.getRole(senderName);
          if (tPlayerRole.getPower() > Role.MOD.getPower()) {
            if (isRegionMember(pluginManager, player, senderName)) {
              team.getTeamHome().setHome(player.getLocation());
              sender.sendMessage(ChatColor.GREEN + "Estableciste la home correctamente!");
            } else {
              sender.sendMessage(ChatColor.RED + "No eres dueño/miembro de la region!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "Debes ser lider/colider para realizar esta accion!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
    }
  }
}
