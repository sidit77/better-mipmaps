package com.github.sidit77.bettermipmaps.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SpriteContents.class)
public interface SpriteContentsAccessor {

    @Accessor("originalImage")
    NativeImage getOriginalImage();

    @Accessor("additionalMetadata")
    List<MetadataSectionType.WithValue<?>> getAdditionalMetadata();

}
