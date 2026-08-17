package com.mahem.furnace_mod.mod_types;

import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuType {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, "furnace_mod");

    public static final Supplier<MenuType<ForgeFurnaceMenu>> FORGE_FURNACE_MENU =
            MENU_TYPES.register("forge_furnace", () ->
                    new MenuType<>(ForgeFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );






    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
