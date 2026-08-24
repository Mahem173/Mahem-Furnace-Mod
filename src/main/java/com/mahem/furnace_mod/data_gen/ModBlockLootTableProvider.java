package com.mahem.furnace_mod.data_gen;

import com.mahem.furnace_mod.ModBlockTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        dropSelf(ModBlockTypes.FORGE_FURNACE.get());
    }
}
