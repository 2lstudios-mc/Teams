package dev._2lstudios.teams.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.teleport.Teleport;
import dev._2lstudios.teams.teleport.TeleportSystem;
import dev._2lstudios.teams.team.Team;

class TeamsHomeCommand {
  TeamsHomeCommand(Server server, TeamPlayerManager tPlayerManager, TeamManager teamManager, TeleportSystem teleportSystem, CommandSender sender,
      boolean homesEnabled) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!homesEnabled) {
        sender.sendMessage(ChatColor.RED + "Las homes estan desactivadas!");
      } else {
        TeamPlayer teamPlayer = tPlayerManager.getPlayer(sender.getName());
        Team team = teamManager.getTeam(teamPlayer.getTeam());
        if (team != null) {
          Location home = team.getTeamHome().getHome(server);
          if (home != null) {
            int seconds;
            if (hasPlayerNear(player)) {
              seconds = 5;
            } else {
              seconds = 1;
            }
            teleportSystem.put(player.getUniqueId(), new Teleport(home, player, seconds));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format("&aEstas siendo teletransportado a la &bHome&a! &7(%s segundos)",
                    new Object[] { Integer.valueOf(seconds) })));
          } else {
            sender.sendMessage(ChatColor.RED + "El team no tiene una home establecida!");
          }
        } else {
          sender.sendMessage(ChatColor.RED + "No eres miembro de ningun team!");
        }
      }
    } else {
      sender.sendMessage(ChatColor.RED + "Ese comando no puede ser utilizado desde la consola!");
    }
  }

  private boolean hasPlayerNear(Player player) {
    Location location = player.getLocation();
    World world = location.getWorld();
    for (Player player1 : world.getPlayers()) {
      if (player1 != player && player1.getWorld() == world) {
        Location location1 = player1.getLocation();
        if (location.distance(location1) < 75.0D)
          return true;
      }
    }
    return false;
  }
}
