package com.github.sidit77.texturesunleashed.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
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
    public SpriteLoader.Preparations stitch(SpriteLoader loader, List<SpriteContents> list, int i, Executor executor) {
        int j = loader.maxSupportedTextureSize;
        Stitcher<SpriteContents> stitcher = new Stitcher<>(j, j, i);
        int k = Integer.MAX_VALUE;
        int l = 1 << i;

        for(SpriteContents spriteContents : list) {
            k = Math.min(k, Math.min(spriteContents.width(), spriteContents.height()));
            int m = Math.min(Integer.lowestOneBit(spriteContents.width()), Integer.lowestOneBit(spriteContents.height()));
            if (m < l) {
                LOGGER.warn(
                        "Texture {} with size {}x{} limits mip level from {} to {}",
                        spriteContents.name(),
                        spriteContents.width(),
                        spriteContents.height(),
                        Mth.log2(l),
                        Mth.log2(m)
                );
                l = m;
            }

            stitcher.registerSprite(spriteContents);
        }

        int n = Math.min(k, l);
        int o = Mth.log2(n);
        int m;
        if (o < i) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", loader.location, i, o, n);
            m = o;
        } else {
            m = i;
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
            crashReportCategory.setDetail("Max Texture Size", j);
            throw new ReportedException(crashReport);
        }

        int p = Math.max(stitcher.getWidth(), loader.minWidth);
        int q = Math.max(stitcher.getHeight(), loader.minHeight);
        Map<ResourceLocation, TextureAtlasSprite> map = loader.getStitchedSprites(stitcher, p, q);
        TextureAtlasSprite textureAtlasSprite = (TextureAtlasSprite)map.get(MissingTextureAtlasSprite.getLocation());
        CompletableFuture<Void> completableFuture;
        if (m > 0) {
            completableFuture = CompletableFuture.runAsync(
                    () -> map.values().forEach(textureAtlasSpritex -> textureAtlasSpritex.contents().increaseMipLevel(m)), executor
            );
        } else {
            completableFuture = CompletableFuture.completedFuture(null);
        }

        return new SpriteLoader.Preparations(p, q, m, textureAtlasSprite, map, completableFuture);
    }

}
