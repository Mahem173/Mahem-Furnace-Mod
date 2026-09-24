package com.mahem.mahems_furnaces.creative_mode;

import com.mahem.mahems_furnaces.FurnaceMod;
import com.mahem.mahems_furnaces.mod_types.ModBlockType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FurnaceMod.MODID);

    public static final Supplier<CreativeModeTab> FURNACE_TAB = CREATIVE_MODE_TABS.register("furnace_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.furnace_mod.furnace_blocks"))
            .icon(() -> new ItemStack(ModBlockType.FORGE_FURNACE.get()))
            .displayItems((itemDisplayParameters, output)->{
                output.accept(ModBlockType.FORGE_FURNACE);
               /* output.accept(ModBlockType.REFINED_FURNACE_ITEM);
                output.accept(ModBlockType.PRESSURISED_FURNACE_ITEM);
                output.accept(ModBlockType.LAVA_FURNACE_ITEM);
                output.accept(ModBlockType.BRITTLE_FURNACE_ITEM);
                output.accept(ModBlockType.METALLURGIC_FURNACE_ITEM);
                output.accept(ModBlockType.WISE_FURNACE_ITEM);
                output.accept(ModBlockType.ADAPTING_FURNACE_ITEM);
                output.accept(ModBlockType.INFLAMMABLE_FURNACE_ITEM);
                output.accept(ModBlockType.CAPITALIST_FURNACE_ITEM);
                output.accept(ModBlockType.DIRTY_FURNACE_ITEM);
                output.accept(ModBlockType.PRECARIOUS_FURNACE_ITEM);
                output.accept(ModBlockType.WEIRD_FURNACE_ITEM);
                output.accept(ModBlockType.COMPILED_FURNACE_ITEM); */
            }).build());

    public static void register(IEventBus eventBus) {CREATIVE_MODE_TABS.register(eventBus);};
}