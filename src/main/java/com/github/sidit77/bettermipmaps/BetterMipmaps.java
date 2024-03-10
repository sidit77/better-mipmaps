package com.github.sidit77.bettermipmaps;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collections;
import java.util.Set;

public class BetterMipmaps implements ModInitializer {

    public static Set<ResourceLocation> UPSCALE_WHITELIST = Collections.singleton(InventoryMenu.BLOCK_ATLAS);

    @Override
    public void onInitialize() {

    }
}
