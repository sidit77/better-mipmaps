package com.github.sidit77.bettermipmaps.client;

import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;

import java.util.Optional;

public interface SpriteContentsExtension {
    Optional<AnimationMetadataSection> better_mipmaps$getAnimationMetadata();
    Optional<TextureMetadataSection> better_mipmaps$getTextureMetadata();
}
