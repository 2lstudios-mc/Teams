package dev._2lstudios.teams.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.managers.TeamsManager;
import dev._2lstudios.teams.team.TeamPlayer;
import dev._2lstudios.teams.teleport.TeleportSystem;
import dev._2lstudios.teams.team.Team;

public class PlayerQuitListener implements Listener {
  private final TeamPlayerManager tPlayerManager;
  private final TeamManager teamManager;
  private final TeleportSystem teleportSystem;

  public PlayerQuitListener(TeamsManager teamsManager) {
    this.tPlayerManager = teamsManager.getTeamPlayerManager();
    this.teamManager = teamsManager.getTeamManager();
    this.teleportSystem = teamsManager.getTeleportSystem();
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    TeamPlayer teamPlayer = this.tPlayerManager.getPlayer(playerName);
    Team team = this.teamManager.getTeam(teamPlayer.getTeam());
    teleportSystem.remove(player);
    teamPlayer.setOnline(false);
    if (team != null) {
      team.sendMessage(
          ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName() + "&c se ha desconectado!"));
      team.getTeamMembers().getOnline().remove(playerName);
    }
  }
}
