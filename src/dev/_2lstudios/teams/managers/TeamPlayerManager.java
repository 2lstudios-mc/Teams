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
import dev._2lstudios.teams.utils.JSONUtil;

public class TeamPlayerManager {
  private final JSONUtil jsonUtil;
  private final Plugin plugin;
  private final Map<String, TeamPlayer> tPlayerMap;

  TeamPlayerManager(final JSONUtil jsonUtil, final Plugin plugin) {
    this.jsonUtil = jsonUtil;
    this.plugin = plugin;
    this.tPlayerMap = new HashMap<>();
  }

  public TeamPlayer getPlayer(final String name) {
    if (name != null && !name.trim().isEmpty()) {
      TeamPlayer teamPlayer;
      if (this.tPlayerMap.containsKey(name)) {
        teamPlayer = this.tPlayerMap.get(name);
      } else {
        OfflinePlayer offlinePlayer;
        final Server server = this.plugin.getServer();
        final Player onlinePlayer = server.getPlayer(name);
        if (onlinePlayer != null) {
          offlinePlayer = onlinePlayer;
        } else {
          offlinePlayer = this.plugin.getServer()
              .getOfflinePlayer(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()));
        }
        teamPlayer = new TeamPlayer(this.jsonUtil, offlinePlayer, name);
        this.tPlayerMap.put(name, teamPlayer);
      }
      teamPlayer.update();
      return teamPlayer;
    }
    return null;
  }

  public void save(final boolean ignoreOnline) {
    final Collection<TeamPlayer> tPlayerMapValues = this.tPlayerMap.values();
    for (final TeamPlayer teamPlayer : new HashSet<>(tPlayerMapValues)) {
      if (ignoreOnline || !teamPlayer.isOnline()) {
        if (teamPlayer.isChanged())
          teamPlayer.save();
        if (teamPlayer.lastUpdate() > 60000L)
          tPlayerMapValues.remove(teamPlayer);
      }
    }
  }
}
