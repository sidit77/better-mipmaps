package com.github.sidit77.bettermipmaps.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Options.class)
abstract class OptionsMixin {

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            ordinal = 14,
            target = "Lnet/minecraft/client/OptionInstance;<init>(Ljava/lang/String;Lnet/minecraft/client/OptionInstance$TooltipSupplier;Lnet/minecraft/client/OptionInstance$CaptionBasedToString;Lnet/minecraft/client/OptionInstance$ValueSet;Ljava/lang/Object;Ljava/util/function/Consumer;)V"),
        index = 3
    )
    public <T> OptionInstance.ValueSet<T> increaseMaxMipmapLevel(OptionInstance.ValueSet<T> valueSet) {
        OptionInstance.IntRange range = (OptionInstance.IntRange)valueSet;
        assert range.minInclusive() == 0 && range.maxInclusive() == 4;
        return (OptionInstance.ValueSet<T>) new OptionInstance.IntRange(0, 10);
    }

}
