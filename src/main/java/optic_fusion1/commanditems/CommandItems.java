package optic_fusion1.commanditems;

import optic_fusion1.commanditems.command.CommandItemCommand;
import optic_fusion1.commanditems.listener.PlayerInteractEventListener;
import optic_fusion1.commanditems.listener.PlayerJoinQuitEventListener;
import optic_fusion1.commanditems.user.CommandItemUser;
import optic_fusion1.commanditems.user.CommandItemUserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandItems extends JavaPlugin {

    private CommandItemUserManager userManager;

    @Override
    public void onEnable() {
        userManager = new CommandItemUserManager(getDataFolder());
        registerListeners(new PlayerInteractEventListener(this), new PlayerJoinQuitEventListener(userManager));
        getCommand("commanditem").setExecutor(new CommandItemCommand(this));
    }

    @Override
    public void onDisable() {
        for (CommandItemUser user : userManager.getUsers()) {
            user.save();
        }
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listenr : listeners) {
            pluginManager.registerEvents(listenr, this);
        }
    }

    public CommandItemUserManager userManager() {
        return userManager;
    }

}
