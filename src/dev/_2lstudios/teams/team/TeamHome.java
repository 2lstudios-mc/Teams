package dev._2lstudios.teams.team;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

public class TeamHome {
  private Vector vector;
  
  private String world;
  
  public void setHome(Location location) {
    this.vector = location.toVector();
    this.vector.setX((int)this.vector.getX()).setY((int)this.vector.getY()).setZ((int)this.vector.getZ());
    this.world = location.getWorld().getName();
  }
  
  public Location getHome(Server server) {
    double x = this.vector.getX();
    double y = this.vector.getY();
    double z = this.vector.getZ();
    if (this.world != null && x != 0.0D && y != 0.0D && z != 0.0D)
      return new Location(server.getWorld(this.world), x, y, z); 
    return null;
  }
  
  public void load(JSONObject jsonObject) {
    JSONObject homeObject = (JSONObject)jsonObject.getOrDefault("home", new JSONObject());
    double x = Double.parseDouble(homeObject.getOrDefault("x", Integer.valueOf(0)).toString());
    double y = Double.parseDouble(homeObject.getOrDefault("y", Integer.valueOf(0)).toString());
    double z = Double.parseDouble(homeObject.getOrDefault("z", Integer.valueOf(0)).toString());
    this.world = (String)homeObject.getOrDefault("world", null);
    this.vector = new Vector(x, y, z);
  }
  
  @Deprecated
  public void load(FileConfiguration fileConfiguration) {
    this.world = fileConfiguration.getString("home.world", null);
    if (fileConfiguration.contains("home.vector")) {
      this.vector = fileConfiguration.getVector("home.vector");
    } else {
      this.vector = new Vector(0, 0, 0);
    } 
  }
  
  public void save(JSONObject jsonObject) {
    JSONObject homeObject = new JSONObject();
    homeObject.put("world", this.world);
    homeObject.put("x", Double.valueOf(this.vector.getX()));
    homeObject.put("y", Double.valueOf(this.vector.getY()));
    homeObject.put("z", Double.valueOf(this.vector.getZ()));
    jsonObject.put("home", homeObject);
  }
}
