package com.mahem.mahems_furnaces.mod_types;

import com.mahem.mahems_furnaces.block_entities.ForgeFurnaceBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.mahem.mahems_furnaces.mod_types.ModBlockType.FORGE_FURNACE;

public class ModBlockEntityType {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "furnace_mod");

    public static final Supplier<BlockEntityType<ForgeFurnaceBlockEntity>> FORGE_FURNACE_ENTITY =
            BLOCK_ENTITY_TYPES.register("forge_furnace", () -> new BlockEntityType<>(ForgeFurnaceBlockEntity::new, FORGE_FURNACE.get()));

    public static void register(IEventBus eventBus) {
        ModBlockEntityType.BLOCK_ENTITY_TYPES.register(eventBus);
    }
}