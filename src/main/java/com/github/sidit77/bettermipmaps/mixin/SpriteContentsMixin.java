package com.github.sidit77.bettermipmaps.mixin;

import com.github.sidit77.bettermipmaps.client.SpriteContentsExtension;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(SpriteContents.class)
public abstract class SpriteContentsMixin implements SpriteContentsExtension {

    @Unique
    private Optional<AnimationMetadataSection> animationMetadata;

    @Inject(method = "<init>(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/metadata/animation/FrameSize;Lcom/mojang/blaze3d/platform/NativeImage;Ljava/util/Optional;Ljava/util/List;)V", at = @At("TAIL"))
    public void saveAnimationMetadata(ResourceLocation resourceLocation,
                                      FrameSize frameSize,
                                      NativeImage nativeImage,
                                      Optional<AnimationMetadataSection> optional,
                                      List<MetadataSectionType.WithValue<?>> list,
                                      CallbackInfo ci) {
        animationMetadata = optional;
    }

    @Override
    @Unique
    public Optional<AnimationMetadataSection> better_mipmaps$getAnimationMetadata() {
        return animationMetadata;
    }

}
