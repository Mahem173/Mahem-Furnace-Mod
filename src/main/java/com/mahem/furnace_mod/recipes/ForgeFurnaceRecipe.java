package com.mahem.furnace_mod.recipes;

import com.google.gson.JsonObject;
import com.mahem.furnace_mod.mod_types.ModRecipeType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import static com.mahem.furnace_mod.mod_types.ModRecipeType.FORGE_FURNACE_CATEGORY;


public class ForgeFurnaceRecipe implements Recipe<SingleRecipeInput> {
    private Ingredient input = null;
    private final ItemStack result;
    private RecipeBookCategory RecipeBookCategory;

    public ForgeFurnaceRecipe(ItemStack result,  Ingredient input) {
        this.input = input;
        this.result = result;
    }
    public Ingredient getInput() {return this.input;}
    public ItemStack getResult() {return this.result;}

    public static void register() {
        Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY,
                Identifier.fromNamespaceAndPath("furnace_mod", "forge_furnace"),
                FORGE_FURNACE_CATEGORY);
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.input.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return this.result.copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return ModRecipeType.FORGE_FURNACE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return ModRecipeType.FORGE_FURNACE_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return FORGE_FURNACE_CATEGORY;
    }

    public static class Serializer implements RecipeSerializer<ForgeFurnaceRecipe> {

        // 1. Public No-Arg Constructor (Required for ::new)
        public Serializer() {
        }

        //@Override
        private static final MapCodec<ForgeFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("input").forGetter(ForgeFurnaceRecipe::getInput),
                ItemStack.CODEC.fieldOf("result").forGetter(ForgeFurnaceRecipe::getResult),
                Codec.INT.fieldOf("cook_time").forGetter(r -> r.result.getBurnTime()) // Example extra field
        ).apply(inst, ForgeFurnaceRecipe::new));

        // 2. Define the StreamCodec (Handles network syncing)
        private static final StreamCodec<RegistryFriendlyByteBuf, ForgeFurnaceRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, ForgeFurnaceRecipe::getInput,
                        ItemStack.STREAM_CODEC, ForgeFurnaceRecipe::getResult,
                        ByteBufCodecs.INT, r -> r.result.getBurnTime(),
                        ForgeFurnaceRecipe::new
                );

        @Override
        public MapCodec<ForgeFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ForgeFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}