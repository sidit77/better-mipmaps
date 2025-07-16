package com.github.sidit77.bettermipmaps.compat.sodium.mixin;

import com.github.sidit77.bettermipmaps.Constants;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.Option;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
abstract class SodiumGameOptionPagesMixin {

    @SuppressWarnings("unchecked")
    @WrapOperation(method = "quality", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionImpl$Builder;build()Lnet/caffeinemc/mods/sodium/client/gui/options/OptionImpl;"), remap = false)
    private static <S, T> OptionImpl<S, T> increaseMipmapLevels(OptionImpl.Builder builder, Operation<OptionImpl<S, T>> factory){
        OptionImpl<S, T> option = factory.call(builder);
        if(!option.getName().getString().equals(Minecraft.getInstance().options.mipmapLevels().toString()))
            return option;
        return builder
                .setControl(o -> new SliderControl((Option<Integer>) o, 0, Constants.MAX_MIPMAP_LEVEL, 1, ControlValueFormatter.multiplier()))
                .build();
    }

}
