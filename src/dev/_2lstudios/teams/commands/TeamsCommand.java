package dev._2lstudios.teams.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.teleport.TeleportSystem;

public class TeamsCommand implements CommandExecutor {
  private final Plugin plugin;
  private final Server server;
  private final Economy economy;
  private final TeamManager teamManager;
  private final TeamPlayerManager teamPlayerManager;
  private final TeleportSystem teleportSystem;
  private final boolean homesEnabled;

  public TeamsCommand(Plugin plugin, Economy economy, TeamsManager teamsManager, boolean homesEnabled) {
    this.plugin = plugin;
    this.server = plugin.getServer();
    this.economy = economy;
    this.teamManager = teamsManager.getTeamManager();
    this.teamPlayerManager = teamsManager.getTeamPlayerManager();
    this.teleportSystem = teamsManager.getTeleportSystem();
    this.homesEnabled = homesEnabled;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    this.server.getScheduler().runTaskAsynchronously(this.plugin, () -> {
      if (args.length < 1) {
        new TeamsHelpCommand(sender, label);
      } else {
        final PluginManager pluginManager = server.getPluginManager();

        args[0] = args[0].toLowerCase();

        if (args[0].equals("help")) {
          new TeamsHelpCommand(sender, label);
        } else if (args[0].equals("leader")) {
          new TeamsLeaderCommand(plugin, sender, teamManager, teamPlayerManager, label, args);
        } else if (args[0].equals("claim")) {
          new TeamsClaimCommand(pluginManager, sender);
        } else if (args[0].equals("chat") || args[0].equals("c")) {
          new TeamsChatCommand(teamPlayerManager, sender);
        } else if (args[0].equals("tl")) {
          new TeamsTellLocationCommand(teamPlayerManager, teamManager, sender);
        } else if (args[0].equals("list")) {
          new TeamsListCommand(teamManager, sender, args);
        } else if (args[0].equals("show") || args[0].equals("who")) {
          new TeamsShowCommand(teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("create")) {
          new TeamsCreateCommand(teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("delete")) {
          new TeamsDeleteCommand(teamPlayerManager, teamManager, sender);
        } else if (args[0].equals("pvp")) {
          new TeamsPvpCommand(teamPlayerManager, teamManager, sender);
        } else if (args[0].equals("invite")) {
          new TeamsInviteCommand(server, teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("deinvite")) {
          new TeamsDeinviteCommand(server, teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("join")) {
          new TeamsJoinCommand(teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("leave")) {
          new TeamsLeaveCommand(teamPlayerManager, teamManager, sender);
        } else if (args[0].equals("kick")) {
          new TeamsKickCommand(server, teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("sethome")) {
          new TeamsSethomeCommand(pluginManager, teamPlayerManager, teamManager, sender, homesEnabled);
        } else if (args[0].equals("home")) {
          new TeamsHomeCommand(server, teamPlayerManager, teamManager, teleportSystem, sender, homesEnabled);
        } else if (args[0].equals("promote")) {
          new TeamsPromoteCommand(server, teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("demote")) {
          new TeamsDemoteCommand(server, teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("deposit") || args[0].equals("d")) {
          new TeamsDepositCommand(economy, teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("withdraw") || args[0].equals("w")) {
          new TeamsWithdrawCommand(economy, teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("description") || args[0].equals("desc")) {
          new TeamsDescriptionCommand(teamPlayerManager, teamManager, sender, args);
        } else if (args[0].equals("ally")) {
          new TeamsAllyCommand(teamPlayerManager, teamManager, sender, label, args);
        } else if (args[0].equals("rename")) {
          new TeamsRenameCommand(teamPlayerManager, teamManager, sender, label, args);
        } else {
          sender.sendMessage(
              ChatColor.RED + "Comando desconocido. Usa /" + label + " help para ver una lista de comandos!");
        }
      }
    });
    return true;
  }
}
