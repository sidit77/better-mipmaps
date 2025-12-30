package com.github.sidit77.bettermipmaps.compat.sodium.mixin;

import com.github.sidit77.bettermipmaps.BetterMipmaps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.caffeinemc.mods.sodium.api.config.structure.IntegerOptionBuilder;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Field;
import java.util.Optional;

@Mixin(value = SodiumConfigBuilder.class, remap = false)
abstract class SodiumConfigBuilderMixin {

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private static final Identifier MIPMAP_LEVELS_ID = Identifier.parse("sodium:quality.mipmap_levels");

    @SuppressWarnings("unchecked")
    @WrapOperation(method = "buildQualityPage", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/config/structure/IntegerOptionBuilder;setRange(III)Lnet/caffeinemc/mods/sodium/api/config/structure/IntegerOptionBuilder;"), remap = false)
    private static <S, T> IntegerOptionBuilder increaseMipmapLevels(IntegerOptionBuilder instance, int min, int max, int steps, Operation<IntegerOptionBuilder> original){
        var field = getFieldRecursive(instance.getClass(), "id");
        if (field.isPresent()) {
            try {
                field.get().setAccessible(true);
                Identifier id = (Identifier) field.get().get(instance);
                if(MIPMAP_LEVELS_ID.equals(id)) {
                    return instance.setRange(0, BetterMipmaps.MAX_MIPMAP_LEVEL, 1);
                }
            } catch (IllegalAccessException e) {
                LOGGER.warn("Failed to access id field", e);
            }
        }
        return instance.setRange(min, max, steps);
    }

    @Unique
    private static Optional<Field> getFieldRecursive(Class<?> clazz, String name) {
        try {
            return Optional.of(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            return Optional.ofNullable(clazz.getSuperclass()).flatMap(c -> getFieldRecursive(c, name));
        }
    }

    /*
    private void test() {
        ConfigBuilder builder = null;
        var page = (OptionPageBuilderImpl)builder.createOptionPage();
        var option = builder.createIntegerOption(null);
        option.
    }

     */

}
