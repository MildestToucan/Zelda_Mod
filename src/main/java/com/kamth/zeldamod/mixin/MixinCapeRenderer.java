package com.kamth.zeldamod.mixin;

import com.kamth.zeldamod.ZeldaMod;
import com.kamth.zeldamod.item.ZeldaItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
abstract class MixinCapeRenderer extends Player {
    @Unique
    private static final ResourceLocation zeldamod$TEXTURE = new ResourceLocation(ZeldaMod.MOD_ID, "textures/models/armor/hylian_cape.png");

    @Unique
    private static final ResourceLocation zeldamod$DEITY = new ResourceLocation(ZeldaMod.MOD_ID, "textures/models/skins/fierce_deity.png");

    public MixinCapeRenderer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }


    /**
     * Sets the player as having a loaded cape if they have a cape accessory equipped and visible.
     */
    // Whilst ModifyReturnValue means that if another mod cancels the method we may be ignored, it prevents us inadvertently,
    // causing the same issue to another mod who may inject operations other than changing the return value.
    @ModifyReturnValue(method = "isCapeLoaded", at = @At("RETURN"))
    private boolean setCapeLoadedForCapeAccessory(boolean original) {
        return original || this.zeldamod$isWearingCapeAccessory();
    }

    @ModifyReturnValue(method = "getCloakTextureLocation", at = @At("RETURN"))
    private ResourceLocation setCloakTextureLocForCapeAccessory(ResourceLocation original) {
        if (this.zeldamod$isWearingCapeAccessory()) {
            return zeldamod$TEXTURE;
        }
        return original;
    }


    // Helper method for checking if a cape accessory is equipped and visible.
    @Unique
    private boolean zeldamod$isWearingCapeAccessory() {
        return this.getItemBySlot(EquipmentSlot.HEAD).is(ZeldaItems.HYLIAN_HOOD.get());
    }

    // Replaced by setCapeLoadedForCapeAccessory
//    @Inject(at = @At("HEAD"), method = "isCapeLoaded", cancellable = true)
//    private void isCapeLoaded(CallbackInfoReturnable<Boolean> cir) {
//        if (this.zeldamod$isWearingCapeAccessory()) {
//            cir.setReturnValue(true);
//        }
//    }

    // Replaced by setCloakTextureLocForCapeAccessory
//    @Inject(at = @At("HEAD"), method = "getCloakTextureLocation", cancellable = true)
//    private void getCloakTextureLocation(CallbackInfoReturnable<ResourceLocation> cir) {
//        if (this.zeldamod$isWearingCapeAccessory()) {
//            cir.setReturnValue(zeldamod$TEXTURE);
//        }
//    }





//    @Inject(at = @At("HEAD"), method = "getSkinTextureLocation", cancellable = true)
//    private void getSkinLocation(CallbackInfoReturnable<ResourceLocation> cir){
//        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
//        if (player.getItemBySlot(EquipmentSlot.HEAD).is(ZeldaItems.FIERCE_DEITY_MASK.get())) {
//            ResourceLocation texture = DEITY;
//            cir.setReturnValue(texture);
//        }
//    }
}








