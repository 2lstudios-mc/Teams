package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TPlayer;
import dev._2lstudios.teams.team.Team;

public class PlayerQuitListener implements Listener {
  private final TPlayerManager tPlayerManager;

  private final TeamManager teamManager;

  public PlayerQuitListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    TPlayer tPlayer = this.tPlayerManager.getPlayer(playerName);
    Team team = this.teamManager.getTeam(tPlayer.getTeam());
    tPlayer.setTeleportTask(null);
    tPlayer.setOnline(false);
    if (team != null) {
      team.sendMessage(
          ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName() + "&c se ha desconectado!"));
      team.getTeamMembers().getOnline().remove(playerName);
    }
  }
}
