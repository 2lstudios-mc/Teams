package dev._2lstudios.teams.teleport;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportSystem implements Runnable {
    private final Map<UUID, Teleport> teleports = new ConcurrentHashMap<>();

    public Teleport put(final UUID key, final Teleport value) {
        return this.teleports.put(key, value);
    }

    public Teleport remove(final UUID key) {
        return this.teleports.remove(key);
    }

    public Teleport remove(final Player player) {
        return remove(player.getUniqueId());
    }

    @Override
    public void run() {
        final Iterator<Teleport> teleportsIterator = teleports.values().iterator();

        while (teleportsIterator.hasNext()) {
            final Teleport teleport = teleportsIterator.next();

            if (teleport.tick() <= 0) {
                teleportsIterator.remove();

                final Player player = teleport.getPlayer();
                final Location location = teleport.getLocation();

                if (player != null && player.isOnline()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.teleport(location);
                    player.sendMessage(ChatColor.GREEN + "Fuiste teletransportado correctamente!");
                }
            }
        }
    }
}
