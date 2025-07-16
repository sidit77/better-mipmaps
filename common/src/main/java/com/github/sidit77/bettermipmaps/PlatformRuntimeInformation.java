package com.github.sidit77.bettermipmaps;

import java.util.ServiceLoader;

public interface PlatformRuntimeInformation {

    PlatformRuntimeInformation INSTANCE = ServiceLoader
            .load(PlatformRuntimeInformation.class)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Failed to load PlatformRuntimeInformation implementation"));

    static PlatformRuntimeInformation getInstance() {
        return INSTANCE;
    }

    boolean isModLoaded(String modId);

}
