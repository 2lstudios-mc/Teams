package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

public class PlayerJoinListener implements Listener {
  private final TPlayerManager tPlayerManager;

  private final TeamManager teamManager;

  public PlayerJoinListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    TPlayer tPlayer = this.tPlayerManager.getPlayer(playerName);
    Team team = this.teamManager.getTeam(tPlayer.getTeam());
    if (team != null)
      if (team.getMembers().containsKey(playerName)) {
        String displayName = player.getDisplayName();
        if (displayName != null) {
          team.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + displayName + "&a se ha conectado!"));
        } else {
          team.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + playerName + "&a se ha conectado!"));
        }
        team.getTeamMembers().getOnline().add(playerName);
      } else {
        team.removePlayer(tPlayer);
      }
  }
}
