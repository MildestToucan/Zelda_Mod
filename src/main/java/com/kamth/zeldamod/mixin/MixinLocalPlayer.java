package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.item.ZeldaItems;
import com.kamth.zeldamod.util.interfaces.mixin.SwordSpinPlayerData;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


//credit to DeadlyDiamond98 this man is my hero
@Mixin(LocalPlayer.class)
abstract class MixinLocalPlayer {


    @Shadow
    protected abstract boolean isControlledCamera();

    @Unique
    private long zeldamod$rotationStartTick;
    @Unique
    private boolean zeldamod$startedSwordSpin;
    @Unique
    private float zeldamod$originalYaw;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.zeldamod$rotationStartTick = 0;
        this.zeldamod$startedSwordSpin = false;
        this.zeldamod$originalYaw = 0;
    }


    @Unique
    private LocalPlayer legendaryArmory$getPlayer() {
        return (LocalPlayer) (Object) this;
    }

    @Inject(method = "serverAiStep", at = @At("TAIL"))
    private void itemSlowdown(CallbackInfo ci) {

        LocalPlayer player = legendaryArmory$getPlayer();

        if (player.isUsingItem() && !player.isPassenger()  && this.isControlledCamera()
                && player.getUseItem().is(ZeldaItems.PARAGLIDER.get())) {

            player.xxa /= 0.23f; // side
            player.zza /= 0.25f; // front/back
        }

        if (player.isUsingItem() && !player.isPassenger()  && this.isControlledCamera()
                && player.getUseItem().is(ZeldaItems.LENS_OF_TRUTH.get())) {

            player.xxa /= 0.24f; // side
            player.zza /= 0.24f; // front/back
        }
    }

    @Inject(method = "getViewYRot", at = @At("TAIL"), cancellable = true)
    private void rotateSwordSpin(float pPartialTick, CallbackInfoReturnable<Float> cir) {

        LocalPlayer player = legendaryArmory$getPlayer();

        SwordSpinPlayerData swordSpinPlayer = (SwordSpinPlayerData) player;

        if (swordSpinPlayer.zeldamod$isSwordSpinActive()) {

            long currentTick = player.tickCount;

            if (!this.zeldamod$startedSwordSpin) {
                this.zeldamod$rotationStartTick = currentTick;
                this.zeldamod$startedSwordSpin = true;
                this.zeldamod$originalYaw = player.getYRot();
            }

            float ticksElapsed = (currentTick - this.zeldamod$rotationStartTick) + pPartialTick;
            float fraction = ticksElapsed / 10;

            if (fraction >= 1.0F) {
                fraction = 1.0F;
            }

            float newYaw = this.zeldamod$originalYaw + 360.0F * fraction;

            cir.setReturnValue(newYaw);
        } else {
            this.zeldamod$startedSwordSpin = false;
        }
    }
}