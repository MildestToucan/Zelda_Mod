package com.kamth.zeldamod.mixin.swordspin;

import com.kamth.zeldamod.enchantments.SwordSpin;
import com.kamth.zeldamod.util.interfaces.mixin.SwordSpinPlayerData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Code contributed by Deadlydiamond98 (c) 2024 under the MIT License.
// Added here with explicit permission by the original owner.

@Mixin(Player.class)
abstract class MixinPlayer implements SwordSpinPlayerData {

    @Unique
    private boolean zeldamod$swordSwinging;

    @Unique
    private int zeldamod$swordspinTicks;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void populateSwordFields(CallbackInfo ci) {
        this.zeldamod$swordspinTicks = 0;
        this.zeldamod$swordSwinging = false;
    }

    @Unique
    private Player zeldamod$self() {
        return (Player) (Object) this;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        Player player = zeldamod$self();

        this.zeldamod$swordspinTicks = SwordSpin.doSwordSpin(player, this.zeldamod$swordspinTicks, zeldamod$isSwordSpinActive());
    }


    @Override
    public void zeldamod$setSwordSpinActive(boolean bl) {
        this.zeldamod$swordSwinging = bl;
    }
    
    @Override
    public boolean zeldamod$isSwordSpinActive() {
        return this.zeldamod$swordSwinging;
    }
}
