package com.mahem.mahems_furnaces.data_gen;

import com.mahem.mahems_furnaces.FurnaceMod;
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
