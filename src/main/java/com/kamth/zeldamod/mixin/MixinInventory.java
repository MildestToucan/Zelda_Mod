package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.custom.ModTags;
import com.kamth.zeldamod.item.items.bags.BombBagItem;
import com.kamth.zeldamod.item.items.bags.CustomBundleItem;
import com.kamth.zeldamod.item.items.bags.QuiverItem;
import com.kamth.zeldamod.item.items.bags.WalletItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin (Inventory.class)
abstract class MixinInventory implements Container, Nameable {

    @Shadow
    @Final
    public Player player;

    // We use ModifyReturnValue to avoid cancelling another mod's injected operations accidentally, since we only care
    // about changing the return value.
    @ModifyReturnValue(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"))
    private boolean tryBaggingStack(boolean original, ItemStack stack) {
        if (stack.is(ModTags.Items.BOW_AMMO)) {
            if (zeldamod$addItemToBag(this.player, stack, QuiverItem.class)) {
                return true;
            }
        }
        else if (stack.is(ModTags.Items.GEMS)) {
            if (zeldamod$addItemToBag(this.player, stack, WalletItem.class)) {
                return true;
            }
        }
        else if (stack.is(ModTags.Items.BOMBS)) {
            if (zeldamod$addItemToBag(this.player, stack, BombBagItem.class)) {
                return true;
            }
        }
        return original;
    }




    // Removed the unused CallbackInfoReturnable<Boolean> parameter.
    @Unique
    private <T extends CustomBundleItem> boolean zeldamod$addItemToBag(Player player,
                                                                              ItemStack itemStack,
                                                                              Class<T> itemClass) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            // Renamed this variable to prevent confusion with the itemStack param.
            ItemStack stackAti = player.getInventory().getItem(i);
            if (itemClass.isInstance(stackAti.getItem())) {
                T customBundle = (T) stackAti.getItem();
                if (customBundle.getBarWidth(customBundle.getDefaultInstance()) < 13) {
                    int added = customBundle.addToBundle(stackAti, itemStack);
                    if (added > 0) {
                        itemStack.shrink(added);
                        if (itemStack.isEmpty()) {
                            player.awardStat(Stats.ITEM_PICKED_UP.get(itemStack.getItem()), added);
                            player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F,
                                    0.8F + player.level().getRandom().nextFloat() * 0.4F);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Replaced by tryBaggingStacks
    // cir.cancel() calls removed because setReturnValue already cancels
//    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
//    private void addStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//        if (stack.is(ModTags.Items.BOW_AMMO)) {
//            if (zeldamod$addItemToBag(this.player, stack, QuiverItem.class)) {
//                cir.setReturnValue(true);
//            }
//        }
//        else if (stack.is(ModTags.Items.GEMS)) {
//            if (zeldamod$addItemToBag(this.player, stack, WalletItem.class)) {
//                cir.setReturnValue(true);
//            }
//        }
//        else if (stack.is(ModTags.Items.BOMBS)) {
//            if (zeldamod$addItemToBag(this.player, stack, BombBagItem.class)) {
//                cir.setReturnValue(true);
//            }
//        }
//    }
}
