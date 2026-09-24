package com.mahem.mahems_furnaces.mod_types;

import com.mahem.mahems_furnaces.FurnaceMod;
import com.mahem.mahems_furnaces.recipes.ForgeFurnaceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeType {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FurnaceMod.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FurnaceMod.MODID);

    public static final RecipeBookCategory FORGE_FURNACE_CATEGORY = new RecipeBookCategory();

    public static final Supplier<RecipeSerializer<ForgeFurnaceRecipe>> FORGE_FURNACE_SERIALIZER =
            RECIPE_SERIALIZERS.register("forge_furnace",
                    () -> new RecipeSerializer<>(ForgeFurnaceRecipe.CODEC, ForgeFurnaceRecipe.STREAM_CODEC));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ForgeFurnaceRecipe>> FORGE_FURNACE_TYPE =
            RECIPE_TYPES.register("smelting", () -> new RecipeType<ForgeFurnaceRecipe>() {
                @Override
                public String toString() {
                    return "smelting";
                }
            });

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}