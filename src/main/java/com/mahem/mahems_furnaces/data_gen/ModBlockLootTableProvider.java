package com.mahem.mahems_furnaces.data_gen;

import com.mahem.mahems_furnaces.mod_types.ModBlockType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        dropSelf(ModBlockType.FORGE_FURNACE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlockType.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
