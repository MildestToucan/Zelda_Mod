package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.item.ZeldaItems;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Villager.class)
abstract class MixinVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    public MixinVillager(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // TODO: CHANGE MIXIN

    @Definition(id = "getPlayerReputation", method = "Lnet/minecraft/world/entity/npc/Villager;getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I")
    @Expression("? = ?.getPlayerReputation(?)")
    @ModifyVariable(method = "updateSpecialPrices", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    private int increaseReputation(int i, Player player) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(ZeldaItems.POSTMAN_MASK.get())) {
            i = 60;
        }
        else if (player.getItemBySlot(EquipmentSlot.HEAD).is(ZeldaItems.KAFEI_MASK.get())) {
            i = 30;
        }
        return i;
    }
}


















