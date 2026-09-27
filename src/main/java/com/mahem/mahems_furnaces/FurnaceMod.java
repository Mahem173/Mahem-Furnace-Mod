package com.mahem.mahems_furnaces;

import com.mahem.mahems_furnaces.creative_mode.ModCreativeModeTab;
import com.mahem.mahems_furnaces.data_gen.ModDataComponent;
import com.mahem.mahems_furnaces.mod_types.*;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.slf4j.Logger;

import static com.mahem.mahems_furnaces.mod_types.ModBlockEntityType.FORGE_FURNACE_ENTITY;
import static com.mahem.mahems_furnaces.mod_types.ModBlockType.*;

@Mod(FurnaceMod.MODID)
public class FurnaceMod {
    // Key Stats
    public static final String MODID = "mahems_furnaces";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FurnaceMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTab.register(modEventBus);

        ModBlockType.register(modEventBus);
        ModItemType.register(modEventBus);

        ModDataComponent.register(modEventBus);
        ModBlockEntityType.register(modEventBus);

        ModMenuType.register(modEventBus);
        ModRecipeType.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Hoppers are broken lol

    /*@SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                FORGE_FURNACE_ENTITY,
                (be, side) -> VanillaContainerWrapper.of()
        );
    }*/

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // Add the example block item to the functional blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(FORGE_FURNACE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}