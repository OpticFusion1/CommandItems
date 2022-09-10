package optic_fusion1.commanditems.user;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class CommandItemUserManager {

    private static final HashMap<UUID, CommandItemUser> USERS = new HashMap<>();
    private File userDirectory;

    public CommandItemUserManager(File pluginDirectory) {
        userDirectory = new File(pluginDirectory, "users");
        if (!userDirectory.exists()) {
            userDirectory.mkdirs();
        }
    }

    public Collection<CommandItemUser> getUsers() {
        return Collections.unmodifiableCollection(USERS.values());
    }

    public CommandItemUser getUser(Player player) {
        return USERS.get(player.getUniqueId());
    }

    public CommandItemUser addUser(Player player) {
        CommandItemUser user = new CommandItemUser(userDirectory, player);
        USERS.putIfAbsent(player.getUniqueId(), user);
        return user;
    }

    public void removeUser(UUID uniqueId) {
        USERS.remove(uniqueId);
    }

}
