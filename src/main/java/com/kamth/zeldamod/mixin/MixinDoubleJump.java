package com.kamth.zeldamod.mixin;




import com.kamth.zeldamod.item.items.movement.FeatherItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
abstract class MixinDoubleJump extends AbstractClientPlayer {
    @Unique
    private int zeldamod$jumps = 0;
    @Unique
    private boolean zeldamod$lastJumped = false;

    public MixinDoubleJump(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
    }


    @Inject(method = "aiStep()V", at = @At("HEAD"))
    private void doubleJump(CallbackInfo info) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.onGround() || player.onClimbable()) zeldamod$jumps = 1;
        else if (!zeldamod$lastJumped && zeldamod$jumps > 0 && player.getDeltaMovement().y < 0) {
            if (player.input.jumping && !player.getAbilities().flying) {
                if (zeldamod$canPerformJump(player)) {

                    --zeldamod$jumps;
                    player.jumpFromGround();
                    player.resetFallDistance();
                }
            }
        }
        zeldamod$lastJumped = player.input.jumping;
    }

    @Unique
    private boolean zeldamod$canPerformJump(LocalPlayer player) {
        ItemStack mainHandStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHandStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
        return  (mainHandStack.getItem() instanceof FeatherItem && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)
                || offHandStack.getItem() instanceof FeatherItem && !player.isFallFlying() && !player.isPassenger()
                && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION));
    }


}