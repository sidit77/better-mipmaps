package com.github.sidit77.texturesunleashed.mixin;

import com.github.sidit77.texturesunleashed.TexturesUnleashed;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Mixin(SpriteLoader.class)
abstract class SpriteLoaderMixin {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Redirect(
            method = "method_47659",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/SpriteLoader;stitch(Ljava/util/List;ILjava/util/concurrent/Executor;)Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;"))
    public SpriteLoader.Preparations stitch(SpriteLoader loader, List<SpriteContents> list, int requestedMipmapLevels, Executor executor) {
        int maxTextureSize = loader.maxSupportedTextureSize;
        Stitcher<SpriteContents> stitcher = new Stitcher<>(maxTextureSize, maxTextureSize, requestedMipmapLevels);
        int actualMinResolution = Integer.MAX_VALUE;
        int globalMinResolution = 1 << requestedMipmapLevels;

        if(TexturesUnleashed.UPSCALE_WHITELIST.contains(loader.location)) {
            int targetLevel = Math.min(list
                            .stream()
                            .mapToInt(s -> Math.min(Integer.lowestOneBit(s.width()), Integer.lowestOneBit(s.height())))
                            .max()
                            .orElse(1),
                    globalMinResolution);

            LOGGER.info("{}: Max Level {}", loader.location, targetLevel);

            list = list.stream().map(s -> {
                int factor = 0;
                while (Math.max(s.width(), s.height()) << factor < TexturesUnleashed.MAX_UPSCALE_RESOLUTION &&
                        Math.min(Integer.lowestOneBit(s.width()), Integer.lowestOneBit(s.height())) << factor < targetLevel) {
                    factor++;
                }
                if(factor > 0) {
                    LOGGER.info("Upscaling {} ({}x{}) by a factor of {}", s.name(), s.width(), s.height(), factor);
                    s = upscale(s, factor);
                }
                return s;
            }).collect(Collectors.toList());
        }



        for(SpriteContents spriteContents : list) {
            actualMinResolution = Math.min(actualMinResolution, Math.min(spriteContents.width(), spriteContents.height()));
            int localMinResolution = Math.min(Integer.lowestOneBit(spriteContents.width()), Integer.lowestOneBit(spriteContents.height()));
            if (localMinResolution < globalMinResolution) {
                LOGGER.warn(
                        "Texture {} with size {}x{} limits mip level from {} to {}",
                        spriteContents.name(),
                        spriteContents.width(),
                        spriteContents.height(),
                        Mth.log2(globalMinResolution),
                        Mth.log2(localMinResolution)
                );
                globalMinResolution = localMinResolution;
            }

            stitcher.registerSprite(spriteContents);
        }

        int minResolution = Math.min(actualMinResolution, globalMinResolution);
        int maxMipmapLevels = Mth.log2(minResolution);
        int mipmapLevels;
        if (maxMipmapLevels < requestedMipmapLevels) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", loader.location, requestedMipmapLevels, maxMipmapLevels, minResolution);
            mipmapLevels = maxMipmapLevels;
        } else {
            mipmapLevels = requestedMipmapLevels;
        }

        try {
            stitcher.stitch();
        } catch (StitcherException var16) {
            CrashReport crashReport = CrashReport.forThrowable(var16, "Stitching");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Stitcher");
            crashReportCategory.setDetail(
                    "Sprites",
                    var16.getAllSprites()
                            .stream()
                            .map(entry -> String.format(Locale.ROOT, "%s[%dx%d]", entry.name(), entry.width(), entry.height()))
                            .collect(Collectors.joining(","))
            );
            crashReportCategory.setDetail("Max Texture Size", maxTextureSize);
            throw new ReportedException(crashReport);
        }

        int p = Math.max(stitcher.getWidth(), loader.minWidth);
        int q = Math.max(stitcher.getHeight(), loader.minHeight);
        Map<ResourceLocation, TextureAtlasSprite> map = loader.getStitchedSprites(stitcher, p, q);
        TextureAtlasSprite textureAtlasSprite = map.get(MissingTextureAtlasSprite.getLocation());
        CompletableFuture<Void> completableFuture;
        if (mipmapLevels > 0) {
            completableFuture = CompletableFuture.runAsync(
                    () -> map.values().forEach(textureAtlasSpritex -> textureAtlasSpritex.contents().increaseMipLevel(mipmapLevels)), executor
            );
        } else {
            completableFuture = CompletableFuture.completedFuture(null);
        }
        return new SpriteLoader.Preparations(p, q, mipmapLevels, textureAtlasSprite, map, completableFuture);
    }

    @Unique
    private SpriteContents upscale(SpriteContents original, int factor) {
        NativeImage in = original.originalImage;
        NativeImage out = new NativeImage(in.format(), in.getWidth() << factor, in.getHeight() << factor, false);
        for(int x = 0; x < in.getWidth(); x++) {
            for(int y = 0; y < in.getHeight(); y++) {
                out.fillRect(x << factor, y << factor, 1 << factor, 1 << factor, in.getPixelRGBA(x, y));
            }
        }
        return new SpriteContents(original.name(), new FrameSize(original.width() << factor, original.height() << factor), out, original.metadata());
    }

}
