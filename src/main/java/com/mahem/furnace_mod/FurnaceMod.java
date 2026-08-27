package com.mahem.furnace_mod;

import com.mahem.furnace_mod.creative_mode.ModCreativeModeTab;
import com.mahem.furnace_mod.data_gen.ModDataComponent;
import com.mahem.furnace_mod.mod_types.*;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static com.mahem.furnace_mod.mod_types.ModBlockType.*;

@Mod(FurnaceMod.MODID)
public class FurnaceMod {
    // Key Stats
    public static final String MODID = "furnace_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FurnaceMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Sorting will be needed probably
        ModCreativeModeTab.register(modEventBus);

        ModBlockType.register(modEventBus);
        ModItemType.register(modEventBus);

        ModDataComponent.register(modEventBus);
        //ModStats.register(modEventBus);

        //ModSounds.register(modEventBus);
        //ModEffects.register(modEventBus);

        //ModPotions.register(modEventBus);
        //ModVillagers.register(modEventBus);

        ModBlockEntityType.register(modEventBus);
        ModMenuType.register(modEventBus);

        ModRecipeType.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // Add the example block item to the functional blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(FORGE_FURNACE);
            //event.accept(ADAPTING_FURNACE_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}