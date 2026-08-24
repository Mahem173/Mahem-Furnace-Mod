package com.mahem.furnace_mod.creative_mode;

import com.mahem.furnace_mod.FurnaceMod;
import com.mahem.furnace_mod.ModBlockTypes;
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
            .title(Component.translatable("creativetab.furnace_mod.forge_furnace_items"))
            .icon(() -> new ItemStack(ModBlockTypes.FORGE_FURNACE.get()))
            .displayItems((itemDisplayParameters, output)->{
                output.accept(ModBlockTypes.FORGE_FURNACE_ITEM);
                output.accept(ModBlockTypes.REFINED_FURNACE_ITEM);
                output.accept(ModBlockTypes.PRESSURISED_FURNACE_ITEM);
                output.accept(ModBlockTypes.LAVA_FURNACE_ITEM);
                output.accept(ModBlockTypes.BRITTLE_FURNACE_ITEM);
                output.accept(ModBlockTypes.METALLURGIC_FURNACE_ITEM);
                output.accept(ModBlockTypes.WISE_FURNACE_ITEM);
                output.accept(ModBlockTypes.ADAPTING_FURNACE_ITEM);
                output.accept(ModBlockTypes.INFLAMMABLE_FURNACE_ITEM);
                output.accept(ModBlockTypes.CAPITALIST_FURNACE_ITEM);
                output.accept(ModBlockTypes.DIRTY_FURNACE_ITEM);
                output.accept(ModBlockTypes.PRECARIOUS_FURNACE_ITEM);
                output.accept(ModBlockTypes.WEIRD_FURNACE_ITEM);
                output.accept(ModBlockTypes.COMPILED_FURNACE_ITEM);
            }).build());

    public static void register(IEventBus eventBus) {CREATIVE_MODE_TABS.register(eventBus);};

}
