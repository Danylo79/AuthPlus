package dev.dankom.auth.listener;

import dev.dankom.auth.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Profile p = new Profile(e.getPlayer());
    }
}
