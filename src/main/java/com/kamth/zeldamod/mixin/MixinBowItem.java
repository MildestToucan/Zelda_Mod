package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.item.items.bags.QuiverItem;
import com.kamth.zeldamod.util.HelperMethods;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(BowItem.class)
abstract class MixinBowItem extends ProjectileWeaponItem implements Vanishable {
    public MixinBowItem(Properties pProperties) {
        super(pProperties);
    }


    @Inject(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void useQuiverOnShot(ItemStack stack, Level world, LivingEntity entity, int remainingUseTicks, CallbackInfo ci) {
        // We know the entity is a player at this point in the target method, so we can avoid using local capture.
        Player user = (Player) entity;

        ItemStack quiverStack = HelperMethods.findQuiver(user);
        if (quiverStack != null) {
            QuiverItem quiver = (QuiverItem) quiverStack.getItem();
            Optional<ItemStack> arrowStack = quiver.getFirstItem(quiverStack);
            if (arrowStack.isPresent()) {
                if (!user.getAbilities().instabuild) {
                    quiver.removeOneItem(quiverStack, arrowStack.get().getItem());
                }
                user.awardStat(Stats.ITEM_USED.get((BowItem) (Object) this));
                ci.cancel();
            }
        }
    }


    // Replaced by a HelperMethod method.
//    @Unique
//    private ItemStack zeldamod$findQuiver(Player player) {
//        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
//            ItemStack stack = player.getInventory().getItem(i);
//            if (stack.getItem() instanceof QuiverItem) {
//                return stack;
//            }
//        }
//        return null;
//    }


}
