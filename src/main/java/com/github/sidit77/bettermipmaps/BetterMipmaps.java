package com.github.sidit77.bettermipmaps;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class BetterMipmaps implements ModInitializer {

    @SuppressWarnings("deprecation")
    public static Set<ResourceLocation> UPSCALE_WHITELIST = Collections.singleton(TextureAtlas.LOCATION_BLOCKS);
    public static int MAX_MIPMAP_LEVEL = 10;

    @Override
    public void onInitialize() {

    }
}
