package optic_fusion1.commanditems.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Utils {

    private Utils() {

    }

    public static boolean validateItemStack(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

}
