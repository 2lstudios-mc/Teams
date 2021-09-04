package dev._2lstudios.teams.team;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import dev._2lstudios.teams.enums.ChatMode;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.utils.JSONUtil;

public class TeamPlayer {
  private final UUID uuid;
  private final String name;
  private ChatMode chatMode;
  private String team;
  private long lastUpdate;
  private boolean changed;
  private boolean online;

  public TeamPlayer(Plugin plugin, TeamManager teamManager, OfflinePlayer offlinePlayer, String name) {
    this.uuid = offlinePlayer.getUniqueId();
    this.name = name;
    this.chatMode = ChatMode.NORMAL;
    this.changed = false;
    this.online = offlinePlayer.isOnline();
    String dataPath = "%datafolder%/players/" + this.uuid.toString() + ".json";

    deserialize(JSONUtil.get(dataPath));
  }

  public void deserialize(final JSONObject json) {
    setTeam((String) json.getOrDefault("team", null));
  }

  public JSONObject serialize() {
    JSONObject playerData = new JSONObject();

    playerData.put("team", this.team);

    return playerData;
  }

  public void save(boolean sync) {
    String dataPath = "%datafolder%/players/" + this.uuid.toString() + ".json";

    if (this.team != null) {
      JSONUtil.save(dataPath, serialize(), sync);
    } else {
      JSONUtil.delete(dataPath, sync);
    }

    setChanged(false);
  }

  public String getName() {
    return this.name;
  }

  public boolean compareStr(String str1, String str2) {
    return (str1 == null) ? ((str2 == null)) : str1.equals(str2);
  }

  public void setTeam(String team) {
    if (!compareStr(this.team, team)) {
      this.team = team;
      setChanged(true);
    }
  }

  public String getTeam() {
    return this.team;
  }

  public boolean isChanged() {
    return this.changed;
  }

  private void setChanged(boolean changed) {
    this.changed = changed;
  }

  public boolean isOnline() {
    return this.online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public UUID getUUID() {
    return this.uuid;
  }

  public void update() {
    this.lastUpdate = System.currentTimeMillis();
  }

  public long lastUpdate() {
    return System.currentTimeMillis() - this.lastUpdate;
  }

  public void setChatMode(ChatMode chatMode) {
    this.chatMode = chatMode;
  }

  public ChatMode getChatMode() {
    return this.chatMode;
  }
}
