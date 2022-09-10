package optic_fusion1.commanditems.command;

import optic_fusion1.commanditems.CommandItems;
import optic_fusion1.commanditems.user.CommandItemUser;
import optic_fusion1.commanditems.user.CommandItemUserManager;
import optic_fusion1.commanditems.util.StringUtils;
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

    private CommandItemUserManager userManager;
    private NamespacedKey key;

    public CommandItemCommand(CommandItems commandItems) {
        key = new NamespacedKey(commandItems, "command");
        userManager = commandItems.userManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!validPlayer(sender)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("You did not enter enough arguments. Usage: /commanditem <assign <command>/unassign> [-g]");
            return true;
        }
        if (!args[0].equalsIgnoreCase("assign") && !args[0].equalsIgnoreCase("unassign")) {
            player.sendMessage(args[0] + " is not a valid argument. Usage: /commanditem <assign <command>|unassign> [-g]");
            return true;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!validateItemStack(itemStack)) {
            player.sendMessage("You must be holding an item to run this command");
            return true;
        }
        boolean global = args[args.length - 1].equalsIgnoreCase("-g");
        if (args[0].equalsIgnoreCase("assign")) {
            String commandString = StringUtils.join(args, global ? args.length - 1 : args.length);
            if (commandString.startsWith("kill")) {
                if (commandString.contains("@e[type=player]") || commandString.contains(player.getName())) {
                    player.sendMessage("Sorry, but you're unable to assign this command.");
                    return true;
                }
            }
            assign(player, itemStack, commandString, global);
            return true;
        }
        if (args[0].equalsIgnoreCase("unassign")) {
            unassign(player, itemStack, global);
            return true;
        }
        player.sendMessage(args[0] + " is not a valid option");
        return true;
    }

    private boolean validPlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to run this command");
            return false;
        } else if (!player.hasPermission("commanditems.use")) {
            player.sendMessage("You need the 'commanditems.use' permission to use this command");
            return false;
        }
        return true;
    }

    private void assign(Player player, ItemStack itemStack, String command, boolean global) {
        if (global) {
            CommandItemUser user = userManager.getUser(player);
            if (user.hasGlobalCommand(itemStack.getType())) {
                player.sendMessage("You already have a global command assigned to " + itemStack.getType());
                return;
            }
            user.addGlobalCommand(itemStack.getType(), command);
            player.sendMessage("Assigned global command " + command + " to the item material " + itemStack.getType() + ". All " + itemStack.getType() + "'s will run this command");
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.STRING, command);
        itemStack.setItemMeta(itemMeta);
        player.sendMessage("You set the items command to " + command);
    }

    private void unassign(Player player, ItemStack itemStack, boolean global) {
        if (global) {
            CommandItemUser user = userManager.getUser(player);
            if (!user.hasGlobalCommand(itemStack.getType())) {
                player.sendMessage("You don't have any global commands assigned to the material " + itemStack.getType());
                return;
            }
            user.removeGlobalCommand(itemStack.getType());
            player.sendMessage("The material " + itemStack.getType() + " no longer has any commands");
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        if (pdc.has(key, PersistentDataType.STRING)) {
            String itemCommand = pdc.get(key, PersistentDataType.STRING);
            pdc.remove(key);
            itemStack.setItemMeta(itemMeta);
            player.sendMessage("The command " + itemCommand + " has been removed from this item");
            return;
        }
        player.sendMessage("This item does not have any commands assigned to it");
    }

}
