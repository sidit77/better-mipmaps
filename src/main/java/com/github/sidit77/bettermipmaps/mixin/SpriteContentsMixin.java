package com.github.sidit77.bettermipmaps.mixin;

import com.github.sidit77.bettermipmaps.client.SpriteContentsMetadata;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
abstract class SpriteContentsMixin implements SpriteContentsMetadata {

    @Unique
    private AnimationMetadataSection animationMetadataSection;

    @Override
    public AnimationMetadataSection metadata() {
        return this.animationMetadataSection;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveAnimationMetadata(ResourceLocation resourceLocation, FrameSize frameSize, NativeImage nativeImage, AnimationMetadataSection animationMetadataSection, CallbackInfo ci) {
        this.animationMetadataSection = animationMetadataSection;
    }

}
