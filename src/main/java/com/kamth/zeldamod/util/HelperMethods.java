package com.kamth.zeldamod.util;

import com.kamth.zeldamod.enchantments.ZeldaEnchantments;
import com.kamth.zeldamod.item.items.bags.QuiverItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * A class made to be able to centralize various methods that may otherwise be redundantly defined in different classes.
 * Notably, certain Mixin helper methods are redundant.
 */
public abstract class HelperMethods {

    /**
     * Helper method used to look for a quiver in the player's inventory. If the returned stack is not null, its item
     * can safely be casted to {@link QuiverItem}.
     * @param player The player whose inventory the quiver is being searched in.
     * @return An {@link ItemStack} with an Item of type {@link QuiverItem} if a quiver is found.
     * otherwise returns {@code null}
     */
    public static ItemStack findQuiver(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            if (currentStack.getItem() instanceof QuiverItem) {
                return currentStack;
            }
        }
        return null;
    }


    /**
     * Serves as a quick shorthand for {@link ItemStack#getEnchantmentLevel(Enchantment)} checks for sword spin, this
     * check was redundantly defined as a local boolean in many places, and used a method marked as deprecated.
     * @param stack the {@link ItemStack} being checked for sword spin.
     * @return Whether the checked stack has sword spin.
     */
    public static boolean hasSwordSpin(ItemStack stack) {
        return stack.getEnchantmentLevel(ZeldaEnchantments.SWORD_SPIN.get()) > 0;
    }
}
