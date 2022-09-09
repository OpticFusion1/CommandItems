package optic_fusion1.commanditems;

import optic_fusion1.commanditems.command.CommandItemCommand;
import optic_fusion1.commanditems.listener.PlayerInteractEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandItems extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEventListener(this), this);
        getCommand("commanditem").setExecutor(new CommandItemCommand(this));
    }

}
