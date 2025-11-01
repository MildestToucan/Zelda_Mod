package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.item.ZeldaItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;


@Mixin(Fox.class)
abstract class MixinFoxEntity extends Animal {

    protected MixinFoxEntity() {
        super(null,null);
    }

    @ModifyReturnValue(method = "trusts", at = @At("RETURN"))
    private boolean trustWhenWearingKeatonMask(boolean original, UUID uuid) {
        Player player = this.level().getPlayerByUUID(uuid);
        return original || (player != null && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ZeldaItems.KEATON_MASK.get());
    }
}