package com.mahem.furnace_mod.block_entities;

import com.mahem.furnace_mod.mod_types.ModBlockEntityType;
import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;


public class ForgeFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.forge_furnace");
    private final HeatLogic heatLogic = new HeatLogic();

    public ForgeFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityType.FORGE_FURNACE_ENTITY.get(), worldPosition, blockState, RecipeType.BLASTING);
        heatLogic.setAddHeat(50);
    }
    public static void tick(Level level, BlockPos pos, BlockState state, ForgeFurnaceBlockEntity blockEntity) {
        blockEntity.heatTick();
    }

    public void heatTick() {this.heatLogic.conduction();}

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return super.getBurnDuration(fuelValues, itemStack) / this.heatLogic.totalHeat;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new ForgeFurnaceMenu(id, playerInv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}