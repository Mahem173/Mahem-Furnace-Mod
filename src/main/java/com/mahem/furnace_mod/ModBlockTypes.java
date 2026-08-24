package com.mahem.furnace_mod;

import com.mahem.furnace_mod.mod_types.ModItemTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.mahem.furnace_mod.FurnaceMod.*;


// Here are blocks and items serialized
public class ModBlockTypes {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredBlock<Block> FORGE_FURNACE = BLOCKS.registerBlock("forge_furnace",properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> FORGE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("forge_furnace", FORGE_FURNACE);

    public static final DeferredBlock<Block> REFINED_FURNACE = BLOCKS.registerBlock("refined_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> REFINED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("refined_furnace", REFINED_FURNACE);

    public static final DeferredBlock<Block> PRESSURISED_FURNACE = BLOCKS.registerBlock("pressurised_furnace", properties -> new Block(properties
            .strength(5f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> PRESSURISED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("pressurised_furnace", PRESSURISED_FURNACE);

    public static final DeferredBlock<Block> LAVA_FURNACE = BLOCKS.registerBlock("lava_furnace", properties -> new Block(properties
            .strength(6f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> LAVA_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("lava_furnace", LAVA_FURNACE);

    public static final DeferredBlock<Block> BRITTLE_FURNACE = BLOCKS.registerBlock("brittle_furnace", properties -> new Block(properties
            .strength(2f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> BRITTLE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("brittle_furnace", BRITTLE_FURNACE);

    public static final DeferredBlock<Block> METALLURGIC_FURNACE = BLOCKS.registerBlock("metallurgic_furnace", properties -> new Block(properties
            .strength(6f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> METALLURGIC_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("metallurgic_furnace", METALLURGIC_FURNACE);

    public static final DeferredBlock<Block> WISE_FURNACE = BLOCKS.registerBlock("wise_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> WISE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("wise_furnace", WISE_FURNACE);

    public static final DeferredBlock<Block> ADAPTING_FURNACE = BLOCKS.registerBlock("adapting_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> ADAPTING_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("adapting_furnace", ADAPTING_FURNACE);

    public static final DeferredBlock<Block> INFLAMMABLE_FURNACE = BLOCKS.registerBlock("inflammable_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> INFLAMMABLE_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("inflammable_furnace", INFLAMMABLE_FURNACE);

    public static final DeferredBlock<Block> CAPITALIST_FURNACE = BLOCKS.registerBlock("capitalist_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> CAPITALIST_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("capitalist_furnace", CAPITALIST_FURNACE);

    public static final DeferredBlock<Block> DIRTY_FURNACE = BLOCKS.registerBlock("dirty_furnace", properties -> new Block(properties
            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> DIRTY_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("dirty_furnace", DIRTY_FURNACE);

    public static final DeferredBlock<Block> PRECARIOUS_FURNACE = BLOCKS.registerBlock("precarious_furnace", properties -> new Block(properties
            .strength(2f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> PRECARIOUS_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("precarious_furnace", PRECARIOUS_FURNACE);

    public static final DeferredBlock<Block> WEIRD_FURNACE = BLOCKS.registerBlock("weird_furnace", properties -> new Block(properties
            .strength(12f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> WEIRD_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("weird_furnace", WEIRD_FURNACE);

    public static final DeferredBlock<Block> COMPILED_FURNACE = BLOCKS.registerBlock("compiled_furnace", properties -> new Block(properties
            .strength(12f).requiresCorrectToolForDrops().sound(SoundType.IRON)));
    public static final DeferredItem<BlockItem> COMPILED_FURNACE_ITEM = ITEMS.registerSimpleBlockItem("compiled_furnace", COMPILED_FURNACE);


    public static ResourceKey<Block> getRK(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function, Component... components) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn, components);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block, Component... components) {
        ModItemTypes.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                for(var component : components) {
                    builder.accept(component);
                }
                super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
            }
        });
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItemTypes.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus modEventBus) {BLOCKS.register(modEventBus);}
}