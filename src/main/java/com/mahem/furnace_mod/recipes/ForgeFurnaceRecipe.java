package com.mahem.furnace_mod.recipes;

import com.mahem.furnace_mod.mod_types.ModRecipeType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record ForgeFurnaceRecipe(Ingredient inputItem, ItemStackTemplate output) implements Recipe<SingleRecipeInput> {

    public static final MapCodec<ForgeFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(ForgeFurnaceRecipe::inputItem),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(ForgeFurnaceRecipe::output)
            ).apply(instance, ForgeFurnaceRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ForgeFurnaceRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    ForgeFurnaceRecipe::inputItem,

                    ItemStackTemplate.STREAM_CODEC,
                    ForgeFurnaceRecipe::output,

                    ForgeFurnaceRecipe::new);


    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return output.create().copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "Smelting";
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
        return RecipeBookCategories.CRAFTING_MISC;
    }
}