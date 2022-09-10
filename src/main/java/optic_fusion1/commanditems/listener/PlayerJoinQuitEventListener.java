package optic_fusion1.commanditems.listener;

import optic_fusion1.commanditems.user.CommandItemUser;
import optic_fusion1.commanditems.user.CommandItemUserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitEventListener implements Listener {

    private CommandItemUserManager userManager;

    public PlayerJoinQuitEventListener(CommandItemUserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        CommandItemUser user = userManager.addUser(event.getPlayer());
        user.load();
    }
    
    @EventHandler
    public void on(PlayerQuitEvent event) {
        CommandItemUser user = userManager.getUser(event.getPlayer());
        user.save();
        userManager.removeUser(event.getPlayer().getUniqueId());
    }
    
}
