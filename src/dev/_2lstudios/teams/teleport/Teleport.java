package dev._2lstudios.teams.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleport {
    private final Location location;
    private final Player player;
    private int ticks;

    public Teleport(Location location, Player player, int tick) {
        this.location = location;
        this.player = player;
        this.ticks = tick;
    }

    public int tick() {
        return --this.ticks;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
