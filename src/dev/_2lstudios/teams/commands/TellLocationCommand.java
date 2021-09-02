package dev._2lstudios.teams.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;

public class TellLocationCommand implements CommandExecutor {
  private final Plugin plugin;

  private final Server server;

  private final TeamPlayerManager tPlayerManager;

  private final TeamManager teamManager;

  public TellLocationCommand(Plugin plugin, TeamsManager teamsManager) {
    this.plugin = plugin;
    this.server = plugin.getServer();
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    this.server.getScheduler().runTaskAsynchronously(this.plugin, () -> {

    });
    return true;
  }
}
