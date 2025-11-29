package com.github.sidit77.bettermipmaps.client;

import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;

import java.util.Optional;

public interface SpriteContentsExtension {
    Optional<AnimationMetadataSection> better_mipmaps$getAnimationMetadata();
}
