package com.github.sidit77.texturesunleashed;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collections;
import java.util.Set;

public class TexturesUnleashed implements ModInitializer {

    public static int MAX_UPSCALE_RESOLUTION = 128;

    public static Set<ResourceLocation> UPSCALE_WHITELIST = Collections.singleton(InventoryMenu.BLOCK_ATLAS);

    @Override
    public void onInitialize() {

    }
}
