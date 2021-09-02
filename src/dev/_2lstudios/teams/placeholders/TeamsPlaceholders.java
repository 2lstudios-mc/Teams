package dev._2lstudios.teams.placeholders;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;
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
        TeamPlayerManager teamPlayerManager = this.teamsManager.getTeamPlayerManager();
        if (teamPlayerManager != null) {
          TeamPlayer teamPlayer = teamPlayerManager.getPlayer(player.getName());
          if (teamPlayer != null) {
            String teamName = teamPlayer.getTeam();
            Team team = this.teamsManager.getTeamManager().getTeam(teamName);
            if (team != null && team.exists())
              return team.getDisplayName();
            return "";
          }
        }
      } else if (identifier.startsWith("color_")) {
        TeamPlayerManager tPlayerManager = this.teamsManager.getTeamPlayerManager();
        String playerName = player.getName();
        TeamPlayer teamPlayer = tPlayerManager.getPlayer(playerName);
        String teamName = teamPlayer.getTeam();
        if (teamName != null) {
          String[] identifierArray = identifier.split("_");
          if (identifierArray.length > 1) {
            TeamPlayer teamPlayer1 = tPlayerManager.getPlayer(identifierArray[1]);
            if (teamPlayer1 != null) {
              String targetTeamName = teamPlayer1.getTeam();
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
