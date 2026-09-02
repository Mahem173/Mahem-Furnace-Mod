package com.mahem.furnace_mod.block_entities;

import com.mahem.furnace_mod.mod_types.ModBlockEntityType;
import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import com.mahem.furnace_mod.mod_types.ModRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.foreign.SymbolLookup;


public class ForgeFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final Component DEFAULT_NAME = Component.translatable("container.forge_furnace");
    private final HeatLogic heatLogic = new HeatLogic();
    protected boolean litUp;



    public ForgeFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityType.FORGE_FURNACE_ENTITY.get(), worldPosition, blockState, RecipeType.SMELTING);
        this.heatLogic.setAddHeat(10);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ForgeFurnaceBlockEntity blockEntity) {
        blockEntity.litUp = level.getBlockState(pos).getValue(AbstractFurnaceBlock.LIT);
        blockEntity.heatLogic.conduction(blockEntity.litUp);

        int progress = blockEntity.dataAccess.get(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS);
        int total = blockEntity.dataAccess.get(AbstractFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
        float forgeProgress = 0f;

        float heatValue = (float) blockEntity.heatLogic.totalHeat / 50f;
        forgeProgress += (heatValue+10f);
        if (forgeProgress >= 200f) {forgeProgress -= 200f;}

        blockEntity.dataAccess.set(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS, (int) forgeProgress);
        System.out.println("Progress " + progress + "/" + total);
        System.out.println("Heat " + heatValue);
        System.out.println("Heat Multiplier " + forgeProgress);
        System.out.println("Forge Progress " + forgeProgress + "/" + total);

        AbstractFurnaceBlockEntity.serverTick((ServerLevel) level, pos, state, blockEntity);
    }


    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return super.getBurnDuration(fuelValues, itemStack);    //this.heatLogic.totalHeat;
    }


    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new ForgeFurnaceMenu(id, playerInv, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }
}