package optic_fusion1.commanditems.listener;

import optic_fusion1.commanditems.CommandItems;
import optic_fusion1.commanditems.user.CommandItemUser;
import optic_fusion1.commanditems.user.CommandItemUserManager;
import optic_fusion1.commanditems.util.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractEventListener implements Listener {

    private CommandItemUserManager userManager;
    private NamespacedKey key;

    public PlayerInteractEventListener(CommandItems commandItems) {
        key = new NamespacedKey(commandItems, "command");
        userManager = commandItems.userManager();
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR || action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (!Utils.validateItemStack(itemStack)) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        Player player = event.getPlayer();
        if (container.has(key, PersistentDataType.STRING)) {
            String command = container.get(key, PersistentDataType.STRING);
            performCommand(player, command);
            return;
        }
        CommandItemUser user = userManager.getUser(player);
        if (!user.hasGlobalCommand(itemStack.getType())) {
            return;
        }
        performCommand(player, user.getGlobalCommand(itemStack.getType()));
    }

    private void performCommand(Player player, String command) {
        // TODO: Add a fake operator or something so this is still possible without needing to op the player
        boolean wasOp = player.isOp();
        player.setOp(true);
        player.performCommand(command);
        player.setOp(wasOp);
    }

}
