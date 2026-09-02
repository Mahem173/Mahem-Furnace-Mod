package com.mahem.furnace_mod.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;

public class ForgeFurnaceRecipe extends AbstractCookingRecipe {
    public static final MapCodec<ForgeFurnaceRecipe> CODEC = AbstractCookingRecipe.cookingMapCodec(ForgeFurnaceRecipe::new, 200);

    public ForgeFurnaceRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    @Override
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }

    @Override
    protected Item furnaceIcon() {
        return null;
    }
}
