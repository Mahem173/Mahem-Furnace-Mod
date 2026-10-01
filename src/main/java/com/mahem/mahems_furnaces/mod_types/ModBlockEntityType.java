package com.mahem.mahems_furnaces.mod_types;

import com.mahem.mahems_furnaces.block_entities.ForgeFurnaceBlockEntity;
import com.mahem.mahems_furnaces.block_entities.LavaFurnaceBlockEntity;
import com.mahem.mahems_furnaces.block_entities.PressurisedFurnaceBlockEntity;
import com.mahem.mahems_furnaces.block_entities.RefinedFurnaceBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.mahem.mahems_furnaces.mod_types.ModBlockType.*;

public class ModBlockEntityType {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "mahems_furnaces");

    public static final Supplier<BlockEntityType<ForgeFurnaceBlockEntity>> FORGE_FURNACE_ENTITY =
            BLOCK_ENTITY_TYPES.register("forge_furnace", () -> new BlockEntityType<>(ForgeFurnaceBlockEntity::new, FORGE_FURNACE.get()));

    public static final Supplier<BlockEntityType<RefinedFurnaceBlockEntity>> REFINED_FURNACE_ENTITY =
            BLOCK_ENTITY_TYPES.register("refined_furnace", () -> new BlockEntityType<>(RefinedFurnaceBlockEntity::new, REFINED_FURNACE.get()));

    public static final Supplier<BlockEntityType<PressurisedFurnaceBlockEntity>> PRESSURISED_FURNACE_ENTITY =
            BLOCK_ENTITY_TYPES.register("pressurised_furnace", () -> new BlockEntityType<>(PressurisedFurnaceBlockEntity::new, PRESSURISED_FURNACE.get()));

    public static final Supplier<BlockEntityType<LavaFurnaceBlockEntity>> LAVA_FURNACE_ENTITY =
            BLOCK_ENTITY_TYPES.register("lava_furnace", () -> new BlockEntityType<>(LavaFurnaceBlockEntity::new, LAVA_FURNACE.get()));

    public static void register(IEventBus eventBus) {
        ModBlockEntityType.BLOCK_ENTITY_TYPES.register(eventBus);
    }
}