package com.mahem.furnace_mod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.mahem.furnace_mod.FurnaceMod.BLOCKS;
import static com.mahem.furnace_mod.FurnaceMod.ITEMS;


// Here are blocks and items serialized
public class ModBlockTypes {
    public static final DeferredBlock<Block> FORGE_FURNACE = BLOCKS.registerSimpleBlock("forge_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> FORGE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("forge_furnace", FORGE_FURNACE);

    public static final DeferredBlock<Block> REFINED_FURNACE = BLOCKS.registerSimpleBlock("refined_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> REFINED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("refined_furnace", REFINED_FURNACE);

    public static final DeferredBlock<Block> PRESSURISED_FURNACE = BLOCKS.registerSimpleBlock("pressurised_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> PRESSURISED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("pressurised_furnace", PRESSURISED_FURNACE);

    public static final DeferredBlock<Block> LAVA_FURNACE = BLOCKS.registerSimpleBlock("lava_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> LAVA_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("lava_furnace", LAVA_FURNACE);

    public static final DeferredBlock<Block> BRITTLE_FURNACE = BLOCKS.registerSimpleBlock("brittle_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> BRITTLE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("brittle_furnace", BRITTLE_FURNACE);

    public static final DeferredBlock<Block> METALLURGIC_FURNACE = BLOCKS.registerSimpleBlock("metallurgic_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> METALLURGIC_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("metallurgic_furnace", METALLURGIC_FURNACE);

    public static final DeferredBlock<Block> WISE_FURNACE = BLOCKS.registerSimpleBlock("wise_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> WISE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("wise_furnace", WISE_FURNACE);

    public static final DeferredBlock<Block> ADAPTING_FURNACE = BLOCKS.registerSimpleBlock("adapting_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> ADAPTING_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("adapting_furnace", ADAPTING_FURNACE);

    public static final DeferredBlock<Block> INFLAMMABLE_FURNACE = BLOCKS.registerSimpleBlock("inflammable_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> INFLAMMABLE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("inflammable_furnace", INFLAMMABLE_FURNACE);

    public static final DeferredBlock<Block> CAPITALIST_FURNACE = BLOCKS.registerSimpleBlock("capitalist_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> CAPITALIST_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("capitalist_furnace", CAPITALIST_FURNACE);

    public static final DeferredBlock<Block> DIRTY_FURNACE = BLOCKS.registerSimpleBlock("dirty_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> DIRTY_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("dirty_furnace", DIRTY_FURNACE);

    public static final DeferredBlock<Block> PRECARIOUS_FURNACE = BLOCKS.registerSimpleBlock("precarious_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> PRECARIOUS_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("precarious_furnace", PRECARIOUS_FURNACE);

    public static final DeferredBlock<Block> WEIRD_FURNACE = BLOCKS.registerSimpleBlock("weird_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> WEIRD_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("weird_furnace", WEIRD_FURNACE);

    public static final DeferredBlock<Block> COMPILED_FURNACE = BLOCKS.registerSimpleBlock("compiled_furnace", p -> p.mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> COMPILED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("compiled_furnace", COMPILED_FURNACE);

    public static void register(IEventBus modEventBus) {BLOCKS.register(modEventBus);}

}
