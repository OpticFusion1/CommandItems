package optic_fusion1.commanditems.user;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandItemUser {

    private UUID uniqueId;
    // TODO: Support a material having multiple commands assigned to it
    // TODO: Support separate command for left/right click
    private HashMap<Material, String> globalCommandItems = new HashMap<>();
    private File file;
    private FileConfiguration config;

    public CommandItemUser(File userDirectory, Player player) {
        uniqueId = player.getUniqueId();
        file = new File(userDirectory, uniqueId.toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CommandItemUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            for (String path : config.getConfigurationSection("commands").getKeys(false)) {
                Material material = Material.valueOf(path);
                String command = config.getString("commands." + path);
                addGlobalCommand(material, command);
            }
        } catch (Exception e) {
            System.out.println("Failed to load " + file);
        }
    }

    public void save() {
        try {
            for (Map.Entry<Material, String> entry : globalCommandItems.entrySet()) {
                Material key = entry.getKey();
                String value = entry.getValue();
                if (!config.isSet("commands")) {
                    config.createSection("commands");
                }
                ConfigurationSection section = config.getConfigurationSection("commands");
                section.set(key.name(), value);
            }
            config.save(file);
        } catch (IOException ex) {
            System.out.println("Failed to save " + file);
        }
    }

    public Map<Material, String> getGlobalCommandItems() {
        return Collections.unmodifiableMap(globalCommandItems);
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public boolean hasGlobalCommand(Material material) {
        return globalCommandItems.containsKey(material);
    }

    public String getGlobalCommand(Material material) {
        return globalCommandItems.get(material);
    }

    public void addGlobalCommand(Material material, String command) {
        globalCommandItems.putIfAbsent(material, command);
    }

    public void removeGlobalCommand(Material material) {
        globalCommandItems.remove(material);
    }

}
