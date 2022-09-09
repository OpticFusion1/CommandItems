package optic_fusion1.commanditems.command;

import optic_fusion1.commanditems.CommandItems;
import static optic_fusion1.commanditems.util.Utils.validateItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CommandItemCommand implements CommandExecutor {

    private NamespacedKey key;

    public CommandItemCommand(CommandItems commandItems) {
        key = new NamespacedKey(commandItems, "command");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to run this command");
            return true;
        }
        if (!player.hasPermission("commanditems.use")) {
            player.sendMessage("You do not have permission to use this command");
            return true;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (args.length == 2 && args[0].equalsIgnoreCase("assign")) {
            if (validateItemStack(itemStack)) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                pdc.set(key, PersistentDataType.STRING, args[1]);
                itemStack.setItemMeta(itemMeta);
                player.sendMessage("You set the items command to " + args[1]);
                return true;
            }
            player.sendMessage("You're not holding a valid item");
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("unassign")) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
            if (pdc.has(key, PersistentDataType.STRING)) {
                String itemCommand = pdc.get(key, PersistentDataType.STRING);
                pdc.remove(key);
                itemStack.setItemMeta(itemMeta);
                player.sendMessage("The command " + itemCommand + " has been removed from this item");
                return true;
            }
            player.sendMessage("This item does not have any commands assigned to it");
            return true;
        } else {
            player.sendMessage("That's not a valid argument. Usage: /commanditem <assign <command>|unassign>");
        }
        return true;
    }

}
