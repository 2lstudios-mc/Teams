package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import dev._2lstudios.teams.enums.Relation;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.team.Team;
import dev._2lstudios.teams.team.TeamRelations;

public class AsyncPlayerChatListener implements Listener {
  private final TeamPlayerManager tPlayerManager;
  private final TeamManager teamManager;

  public AsyncPlayerChatListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    TeamPlayer teamPlayer = this.tPlayerManager.getPlayer(playerName);
    String teamName = teamPlayer.getTeam();
    if (teamName != null) {
      Team team = this.teamManager.getTeam(teamName);
      if (team.exists()) {
        TeamRelations teamRelations;
        switch (teamPlayer.getChatMode()) {
          case TEAM:
            team.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&aTeam&8] &a" + playerName + "&8:&r " + event.getMessage()));
            event.setCancelled(true);
            break;
          case ALLY:
            teamRelations = team.getTeamRelations();
            team.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&bAlly&8] &a" + playerName + "&8:&r " + event.getMessage()));
            for (String teamName1 : teamRelations.getTeams()) {
              Team team1 = this.teamManager.getTeam(teamName1);
              if (teamRelations.getMutual(team1) == Relation.ALLY)
                team1.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8[&bAlly&8] &b" + playerName + "&8:&r " + event.getMessage()));
            }
            event.setCancelled(true);
            break;
          default:
            break;
        }
      }
    }
  }
}
