package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

class TeamsTellLocationCommand {
  TeamsTellLocationCommand(TPlayerManager tPlayerManager, TeamManager teamManager, CommandSender sender) {
    if (sender instanceof Player) {
      String senderName = sender.getName();
      Player player = (Player) sender;
      TPlayer tPlayer = tPlayerManager.getPlayer(senderName);
      Team team = teamManager.getTeam(tPlayer.getTeam());
      if (team != null) {
        Location location = player.getLocation();
        team.sendMessage(ChatColor.translateAlternateColorCodes('&',
            "&8[&aTeam&8] &b" + senderName + "&8:&e&l TL: &6" + location.getWorld().getEnvironment() + " &a(X: &b"
                + (int) location.getX() + "&a Y: &b" + (int) location.getY() + "&a Z: &b" + (int) location.getZ()
                + "&a)"));
      } else {
        sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
    }
  }
}
