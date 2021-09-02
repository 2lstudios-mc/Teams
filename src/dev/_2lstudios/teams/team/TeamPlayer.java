package dev._2lstudios.teams.team;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import dev._2lstudios.teams.enums.ChatMode;
import dev._2lstudios.teams.enums.Role;
import dev._2lstudios.teams.managers.TeamManager;
import dev._2lstudios.teams.tasks.TeleportTask;
import dev._2lstudios.teams.utils.JSONUtil;

public class TeamPlayer {
  private TeleportTask teleportTask;
  
  private final UUID uuid;
  
  private final String name;
  
  private ChatMode chatMode;
  
  private String team;
  
  private long lastUpdate;
  
  private boolean changed;
  
  private boolean online;
  
  public TeamPlayer(Plugin plugin, TeamManager teamManager, OfflinePlayer offlinePlayer, String name) {
    this.teleportTask = null;
    this.uuid = offlinePlayer.getUniqueId();
    this.name = name;
    this.chatMode = ChatMode.NORMAL;
    this.changed = false;
    this.online = offlinePlayer.isOnline();
    String path = "%datafolder%/players/" + this.uuid.toString() + ".json";
    JSONObject playerJson = JSONUtil.get(path);
    setTeam((String)playerJson.getOrDefault("team", null));
    Team teamInstance = teamManager.getTeam(this.team);
    if (teamInstance != null && teamInstance.exists() && playerJson.containsKey("role")) {
      Role role = Role.valueOf((String)playerJson.get("role"));
      teamInstance.getMembers().put(name, role);
      plugin.getLogger().info("Manually placed player on team because an old role was found.");
    } 
  }
  
  public void save(boolean sync) {
    String path = "%datafolder%/players/" + this.uuid.toString() + ".json";
    JSONObject jsonObject = new JSONObject();
    if (this.team != null) {
      jsonObject.put("team", this.team);
      JSONUtil.save(path, jsonObject, sync);
    } else {
      JSONUtil.delete(path, sync);
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
    if (team != null)
      team = team.toLowerCase(); 
    if (!compareStr(this.team, team)) {
      if (team != null) {
        this.team = team;
      } else {
        this.team = team;
      } 
      setChanged(true);
    } 
  }
  
  public String getTeam() {
    return this.team;
  }
  
  public TeleportTask getTeleportTask() {
    return this.teleportTask;
  }
  
  public void setTeleportTask(TeleportTask teleportTask) {
    this.teleportTask = teleportTask;
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
