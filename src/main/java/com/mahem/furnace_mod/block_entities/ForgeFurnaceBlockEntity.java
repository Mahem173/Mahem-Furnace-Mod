package com.mahem.furnace_mod.block_entities;

import com.mahem.furnace_mod.AbstractHeat;
import com.mahem.furnace_mod.mod_types.ModBlockEntityType;
import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;


public class ForgeFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.forge_furnace");
    private AbstractHeat totalHeat;

    public ForgeFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState, AbstractHeat totalHeat) {
        // Add .get() to resolve the Supplier<BlockEntityType> to BlockEntityType
        super(ModBlockEntityType.FORGE_FURNACE_ENTITY.get(), worldPosition, blockState, RecipeType.BLASTING);
        this.totalHeat = new ;
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return super.getBurnDuration(fuelValues, itemStack) / totalHeat;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new ForgeFurnaceMenu(id, playerInv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}