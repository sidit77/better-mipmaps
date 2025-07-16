package com.github.sidit77.bettermipmaps;

import net.neoforged.fml.loading.LoadingModList;

public class NeoforgePlatfomRuntimeInformation implements PlatformRuntimeInformation {
    @Override
    public boolean isModLoaded(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }
}
