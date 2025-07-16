package com.github.sidit77.bettermipmaps.mixin;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        Constants.LOG.info("This line is printed by the multiloader-test common mixin!");
        Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}
