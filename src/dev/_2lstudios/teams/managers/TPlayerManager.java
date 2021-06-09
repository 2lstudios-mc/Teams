package dev._2lstudios.teams.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.teams.tasks.TeleportTask;
import dev._2lstudios.teams.team.TPlayer;

public class TPlayerManager {
  private final Plugin plugin;
  private final TeamsManager teamsManager;
  private final Map<String, TPlayer> tPlayerMap;
  private final Collection<TeleportTask> teleportTasks;

  TPlayerManager(Plugin plugin, TeamsManager teamsManager) {
    this.plugin = plugin;
    this.teamsManager = teamsManager;
    this.tPlayerMap = new HashMap<>();
    this.teleportTasks = new HashSet<>();
  }

  public TPlayer getPlayer(String name) {
    if (name != null && !name.trim().isEmpty()) {
      TPlayer tPlayer;
      if (this.tPlayerMap.containsKey(name)) {
        tPlayer = this.tPlayerMap.get(name);
      } else {
        OfflinePlayer offlinePlayer;
        Server server = this.plugin.getServer();
        Player onlinePlayer = server.getPlayer(name);
        if (onlinePlayer != null) {
          Player player = onlinePlayer;
        } else {
          offlinePlayer = this.plugin.getServer()
              .getOfflinePlayer(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()));
        }
        tPlayer = new TPlayer(this.plugin, this.teamsManager.getTeamManager(), offlinePlayer, name);
        this.tPlayerMap.put(name, tPlayer);
      }
      tPlayer.update();
      return tPlayer;
    }
    return null;
  }

  public void addTeleportTask(TeleportTask teleportTask) {
    this.teleportTasks.add(teleportTask);
  }

  public void update(boolean ignoreOnline, boolean sync) {
    Collection<TPlayer> tPlayerMapValues = this.tPlayerMap.values();
    for (TPlayer tPlayer : new HashSet(tPlayerMapValues)) {
      if (ignoreOnline || !tPlayer.isOnline()) {
        if (tPlayer.isChanged())
          tPlayer.save(sync);
        if (tPlayer.lastUpdate() > 60000L)
          tPlayerMapValues.remove(tPlayer);
      }
    }
  }

  public void updateTeleports() {
    for (Iterator<TeleportTask> iterator = this.teleportTasks.iterator(); iterator.hasNext();) {
      TeleportTask teleportTask = iterator.next();
      if (teleportTask.update())
        iterator.remove();
    }
  }
}
