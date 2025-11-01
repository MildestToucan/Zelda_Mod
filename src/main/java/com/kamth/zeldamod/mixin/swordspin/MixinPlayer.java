package com.kamth.zeldamod.mixin.swordspin;

import com.kamth.zeldamod.enchantments.SwordSpin;
import com.kamth.zeldamod.util.interfaces.mixin.SwordSpinPlayerData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Code contributed by Deadlydiamond98 (c) 2024 under the MIT License.
// Added here with explicit permission by the original owner.

@Mixin(Player.class)
abstract class MixinPlayer extends LivingEntity implements SwordSpinPlayerData {

    @Unique
    private boolean zeldamod$swordSwinging;

    @Unique
    private int zeldamod$swordspinTicks;

    @Override
    public void zeldamod$setSwordSpinActive(boolean bl) {
        this.zeldamod$swordSwinging = bl;
    }

    @Override
    public boolean zeldamod$isSwordSpinActive() {
        return this.zeldamod$swordSwinging;
    }

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.zeldamod$swordspinTicks = 0;
        this.zeldamod$swordSwinging = false;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        this.zeldamod$swordspinTicks = SwordSpin.doSwordSpin((Player) (Object) this, this.zeldamod$swordspinTicks, zeldamod$isSwordSpinActive());
    }
}