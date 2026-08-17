package com.mahem.furnace_mod;

import com.mahem.furnace_mod.mod_types.ModBlockEntityType;
import com.mahem.furnace_mod.mod_types.ModMenuType;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import static com.mahem.furnace_mod.ModBlockTypes.*;

@Mod(FurnaceMod.MODID)
public class FurnaceMod {
    // Key Stats
    public static final String MODID = "furnace_mod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Here are blocks and items serialized

    // Creative mod tab defining
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("furnace_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.furnace_mod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> FORGE_FURNACE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(FORGE_FURNACE_ITEM.get());
                output.accept(REFINED_FURNACE_ITEM.get());
                output.accept(PRESSURISED_FURNACE_ITEM.get());
                output.accept(LAVA_FURNACE_ITEM.get());
                output.accept(BRITTLE_FURNACE_ITEM.get());
                output.accept(METALLURGIC_FURNACE_ITEM.get());
                output.accept(WISE_FURNACE_ITEM.get());
                output.accept(ADAPTING_FURNACE_ITEM.get());
                output.accept(INFLAMMABLE_FURNACE_ITEM.get());
                output.accept(CAPITALIST_FURNACE_ITEM.get());
                output.accept(DIRTY_FURNACE_ITEM);
                output.accept(PRECARIOUS_FURNACE_ITEM.get());
                output.accept(WEIRD_FURNACE_ITEM.get());
                output.accept(COMPILED_FURNACE_ITEM.get());
                // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public FurnaceMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);


        ModBlockTypes.register(modEventBus);
        ModBlockEntityType.register(modEventBus);
        ModMenuType.register(modEventBus);



        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the functional blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(FORGE_FURNACE_ITEM);
            event.accept(REFINED_FURNACE_ITEM);
            event.accept(PRESSURISED_FURNACE_ITEM);
            event.accept(LAVA_FURNACE_ITEM);
            event.accept(BRITTLE_FURNACE_ITEM);
            event.accept(METALLURGIC_FURNACE_ITEM);
            event.accept(WISE_FURNACE_ITEM);
            event.accept(ADAPTING_FURNACE_ITEM);
            event.accept(INFLAMMABLE_FURNACE_ITEM);
            event.accept(CAPITALIST_FURNACE_ITEM);
            event.accept(DIRTY_FURNACE_ITEM);
            event.accept(PRECARIOUS_FURNACE_ITEM);
            event.accept(WEIRD_FURNACE_ITEM);
            event.accept(COMPILED_FURNACE_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}









