package dev._2lstudios.teams.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev._2lstudios.teams.managers.TPlayerManager;
import dev._2lstudios.teams.team.TPlayer;

public class TeleportTask {
  private final TPlayer tPlayer;
  
  private final Player player;
  
  private final Location location;
  
  private int time;
  
  public TeleportTask(TPlayerManager tPlayerManager, TPlayer tPlayer, Player player, Location location, int time) {
    this.tPlayer = tPlayer;
    this.player = player;
    this.location = location;
    this.time = time;
    tPlayerManager.addTeleportTask(this);
  }
  
  public boolean update() {
    this.time--;
    if (this.tPlayer.getTeleportTask() != this)
      return true; 
    if (this.time < 1) {
      if (this.player != null && this.player.isOnline()) {
        this.player.teleport(this.location);
        this.tPlayer.setTeleportTask(null);
        this.player.sendMessage(ChatColor.GREEN + "Fuiste teletransportado correctamente!");
      } 
      return true;
    } 
    return false;
  }
}
