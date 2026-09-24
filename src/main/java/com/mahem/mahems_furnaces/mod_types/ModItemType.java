package com.mahem.mahems_furnaces.mod_types;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mahem.mahems_furnaces.FurnaceMod.MODID;

// Here go standalone non-block items
public class ModItemType {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);






    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);}
}