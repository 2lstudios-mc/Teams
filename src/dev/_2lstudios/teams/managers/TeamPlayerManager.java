package dev._2lstudios.teams.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.teams.team.TeamPlayer;

public class TeamPlayerManager {
  private final Plugin plugin;
  private final TeamsManager teamsManager;
  private final Map<String, TeamPlayer> tPlayerMap;

  TeamPlayerManager(Plugin plugin, TeamsManager teamsManager) {
    this.plugin = plugin;
    this.teamsManager = teamsManager;
    this.tPlayerMap = new HashMap<>();
  }

  public TeamPlayer getPlayer(String name) {
    if (name != null && !name.trim().isEmpty()) {
      TeamPlayer teamPlayer;
      if (this.tPlayerMap.containsKey(name)) {
        teamPlayer = this.tPlayerMap.get(name);
      } else {
        OfflinePlayer offlinePlayer;
        Server server = this.plugin.getServer();
        Player onlinePlayer = server.getPlayer(name);
        if (onlinePlayer != null) {
          offlinePlayer = onlinePlayer;
        } else {
          offlinePlayer = this.plugin.getServer()
              .getOfflinePlayer(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()));
        }
        teamPlayer = new TeamPlayer(this.plugin, this.teamsManager.getTeamManager(), offlinePlayer, name);
        this.tPlayerMap.put(name, teamPlayer);
      }
      teamPlayer.update();
      return teamPlayer;
    }
    return null;
  }

  public void update(boolean ignoreOnline, boolean sync) {
    Collection<TeamPlayer> tPlayerMapValues = this.tPlayerMap.values();
    for (TeamPlayer teamPlayer : new HashSet<>(tPlayerMapValues)) {
      if (ignoreOnline || !teamPlayer.isOnline()) {
        if (teamPlayer.isChanged())
          teamPlayer.save(sync);
        if (teamPlayer.lastUpdate() > 60000L)
          tPlayerMapValues.remove(teamPlayer);
      }
    }
  }
}
