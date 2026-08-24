package com.mahem.furnace_mod.mod_types;

import com.mahem.furnace_mod.FurnaceMod;
import com.mahem.furnace_mod.recipes.ForgeFurnaceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeType {

    public static final RecipeBookCategory FORGE_FURNACE_CATEGORY =
            new RecipeBookCategory();

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, "furnace_mod");

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "furnace_mod");

    public static  final DeferredHolder<RecipeType<?>, RecipeType<ForgeFurnaceRecipe>> FORGE_FURNACE_TYPE = RECIPE_TYPES.register("forge_furnace", () ->
            RecipeType.simple(Identifier.fromNamespaceAndPath(FurnaceMod.MODID, "forge_furnace")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ForgeFurnaceRecipe>> FORGE_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("forge_furnace", ForgeFurnaceRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}