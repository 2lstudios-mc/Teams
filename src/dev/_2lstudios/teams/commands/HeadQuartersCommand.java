package dev._2lstudios.teams.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.teleport.TeleportSystem;

public class HeadQuartersCommand implements CommandExecutor {
  private final Server server;
  private final TeamPlayerManager tPlayerManager;
  private final TeamManager teamManager;
  private final TeleportSystem teleportSystem;
  private final boolean homesEnabled;

  public HeadQuartersCommand(Plugin plugin, TeamsManager teamsManager, boolean homesEnabled) {
    this.server = plugin.getServer();
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
    this.teleportSystem = teamsManager.getTeleportSystem();
    this.homesEnabled = homesEnabled;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    new TeamsHomeCommand(server, tPlayerManager, teamManager, teleportSystem, sender, homesEnabled);

    return true;
  }
}
