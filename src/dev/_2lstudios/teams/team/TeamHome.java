package dev._2lstudios.teams.team;

import org.bukkit.Location;
import org.bukkit.Server;
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
  
  public void deserialize(JSONObject jsonObject) {
    JSONObject homeObject = (JSONObject)jsonObject.getOrDefault("home", new JSONObject());
    double x = Double.parseDouble(homeObject.getOrDefault("x", Integer.valueOf(0)).toString());
    double y = Double.parseDouble(homeObject.getOrDefault("y", Integer.valueOf(0)).toString());
    double z = Double.parseDouble(homeObject.getOrDefault("z", Integer.valueOf(0)).toString());
    this.world = (String)homeObject.getOrDefault("world", null);
    this.vector = new Vector(x, y, z);
  }

  public JSONObject serialize() {
    JSONObject homeData = new JSONObject();

    homeData.put("world", this.world);
    homeData.put("x", Double.valueOf(this.vector.getX()));
    homeData.put("y", Double.valueOf(this.vector.getY()));
    homeData.put("z", Double.valueOf(this.vector.getZ()));

    return homeData;
  }
}
