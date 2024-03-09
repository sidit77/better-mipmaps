package com.github.sidit77.texturesunleashed.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class TexturesUnleashedClient implements ClientModInitializer {

    private KeyMapping enableMod;

    @Override
    public void onInitializeClient() {
        System.out.println("Hello World!");
        enableMod = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.texturesunleashed.mipmap",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.categories.misc"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (enableMod.consumeClick()) {
                int newLevel = 6;
                client.player.displayClientMessage(Component.literal("Setting mip maps to " + newLevel), false);
                Minecraft.getInstance().updateMaxMipLevel(newLevel);
                Minecraft.getInstance().delayTextureReload();
            }
        });
    }

}
