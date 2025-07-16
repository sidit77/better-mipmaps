package com.github.sidit77.bettermipmaps;

import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformRuntimeInformation implements PlatformRuntimeInformation {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
