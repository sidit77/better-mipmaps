package com.github.sidit77.bettermipmaps.mixin;

import com.github.sidit77.bettermipmaps.client.SpriteContentsExtension;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
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

    @Unique
    private Optional<TextureMetadataSection> textureMetadata;

    @Inject(method = "<init>(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/resources/metadata/animation/FrameSize;Lcom/mojang/blaze3d/platform/NativeImage;Ljava/util/Optional;Ljava/util/List;Ljava/util/Optional;)V", at = @At("TAIL"))
    public void saveAnimationMetadata(Identifier identifier,
                                      FrameSize frameSize,
                                      NativeImage nativeImage,
                                      Optional<AnimationMetadataSection> optional,
                                      List<MetadataSectionType.WithValue<?>> list,
                                      Optional<TextureMetadataSection> optional2,
                                      CallbackInfo ci) {
        animationMetadata = optional;
        textureMetadata = optional2;
    }

    @Override
    @Unique
    public Optional<AnimationMetadataSection> better_mipmaps$getAnimationMetadata() {
        return animationMetadata;
    }

    @Override
    @Unique
    public Optional<TextureMetadataSection> better_mipmaps$getTextureMetadata() {
        return textureMetadata;
    }

}
