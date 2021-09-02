package dev._2lstudios.teams.tasks;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev._2lstudios.teams.managers.TeamPlayerManager;
import dev._2lstudios.teams.team.TeamPlayer;

public class TeleportTask {
  private final TeamPlayer teamPlayer;
  
  private final Player player;
  
  private final Location location;
  
  private int time;
  
  public TeleportTask(TeamPlayerManager tPlayerManager, TeamPlayer teamPlayer, Player player, Location location, int time) {
    this.teamPlayer = teamPlayer;
    this.player = player;
    this.location = location;
    this.time = time;
    tPlayerManager.addTeleportTask(this);
  }
  
  public boolean update() {
    this.time--;
    if (this.teamPlayer.getTeleportTask() != this)
      return true; 
    if (this.time < 1) {
      if (this.player != null && this.player.isOnline()) {
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
        this.player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        this.player.teleport(this.location);
        this.teamPlayer.setTeleportTask(null);
        this.player.sendMessage(ChatColor.GREEN + "Fuiste teletransportado correctamente!");
      } 
      return true;
    } 
    return false;
  }
}
