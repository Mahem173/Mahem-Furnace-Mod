package com.mahem.furnace_mod.data_gen;

import com.mahem.furnace_mod.FurnaceMod;
import com.mahem.furnace_mod.recipes.ForgeFurnaceRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FurnaceMod.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {

    }
}
