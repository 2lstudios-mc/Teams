package dev._2lstudios.teams.placeholders;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class TeamsPlaceholders extends PlaceholderExpansion {
  private final Plugin plugin;
  private final TeamsManager teamsManager;

  public TeamsPlaceholders(Plugin plugin, TeamsManager teamsManager) {
    this.plugin = plugin;
    this.teamsManager = teamsManager;
  }

  public String getIdentifier() {
    return "teams";
  }

  public String getAuthor() {
    return this.plugin.getDescription().getAuthors().toString();
  }

  public String getVersion() {
    return this.plugin.getDescription().getVersion();
  }

  public String onPlaceholderRequest(Player player, String identifier) {
    if (player != null && !identifier.isEmpty()) {
      if (identifier.equalsIgnoreCase("team")) {
        TPlayerManager tPlayerManager = this.teamsManager.getTPlayerManager();
        if (tPlayerManager != null) {
          TPlayer tPlayer = tPlayerManager.getPlayer(player.getName());
          if (tPlayer != null) {
            String teamName = tPlayer.getTeam();
            Team team = this.teamsManager.getTeamManager().getTeam(teamName);
            if (team != null && team.exists())
              return team.getDisplayName();
            return "";
          }
        }
      } else if (identifier.startsWith("color_")) {
        TPlayerManager tPlayerManager = this.teamsManager.getTPlayerManager();
        String playerName = player.getName();
        TPlayer tPlayer = tPlayerManager.getPlayer(playerName);
        String teamName = tPlayer.getTeam();
        if (teamName != null) {
          String[] identifierArray = identifier.split("_");
          if (identifierArray.length > 1) {
            TPlayer tPlayer1 = tPlayerManager.getPlayer(identifierArray[1]);
            if (tPlayer1 != null) {
              String targetTeamName = tPlayer1.getTeam();
              if (targetTeamName != null) {
                TeamManager teamManager = this.teamsManager.getTeamManager();
                Team team = teamManager.getTeam(teamName);
                if (team != null) {
                  Team team1 = teamManager.getTeam(targetTeamName);
                  if (team1 != null) {
                    Relation relation = team.getTeamRelations().getMutual(team1);

                    return relation.getPrefix();
                  }
                }
              }
            }
          }
        }
        return ChatColor.RED.toString();
      }
      return null;
    }
    return null;
  }
}
