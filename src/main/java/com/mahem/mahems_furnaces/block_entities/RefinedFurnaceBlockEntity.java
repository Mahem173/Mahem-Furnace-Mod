package com.mahem.mahems_furnaces.block_entities;

import com.mahem.mahems_furnaces.internal_logic.HeatLogic;
import com.mahem.mahems_furnaces.menus.RefinedFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static com.mahem.mahems_furnaces.blocks.RefinedFurnaceBlock.LIT;
import static com.mahem.mahems_furnaces.mod_types.ModBlockEntityType.REFINED_FURNACE_ENTITY;

public class RefinedFurnaceBlockEntity extends BaseContainerBlockEntity {
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(3) {
        @Override
        protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            RefinedFurnaceBlockEntity.this.setChanged();
        }
    };


    private final int SIZE = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;
    private int litTimeRemaining = 0;
    private int totalLitTime = 0;
    private int bonusCounter = 0;

    private final HeatLogic heatLogic = new HeatLogic();

    public RefinedFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(REFINED_FURNACE_ENTITY.get(), worldPosition, blockState);
        this.heatLogic.setHeatValue(3);
        this.heatLogic.setCeilHeat(9009);
        this.data = new ContainerData() {
            @Override
            public int get(int dataId) {
                return switch (dataId) {
                    case 0 -> RefinedFurnaceBlockEntity.this.progress;
                    case 1 -> RefinedFurnaceBlockEntity.this.maxProgress;
                    case 2 -> RefinedFurnaceBlockEntity.this.litTimeRemaining;
                    case 3 -> RefinedFurnaceBlockEntity.this.totalLitTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int dataId, int value) {
                switch (dataId) {
                    case 0:
                        RefinedFurnaceBlockEntity.this.progress = value;
                        break;
                    case 1:
                        RefinedFurnaceBlockEntity.this.maxProgress = value;
                        break;
                    case 2:
                        RefinedFurnaceBlockEntity.this.litTimeRemaining = value;
                        break;
                    case 3:
                        RefinedFurnaceBlockEntity.this.totalLitTime = value;
                        break;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NonNull Component getDefaultName() {
        return Component.translatable("block.mahems_furnaces.refined_furnace");
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory, @NonNull Player player) {
        return new RefinedFurnaceMenu(containerId, inventory, this);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }



    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("furnace.progress", progress);
        output.putInt("furnace.max_progress", maxProgress);

        output.putChild("inventory", inventory);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("furnace.progress", 0);
        maxProgress = input.getIntOr("furnace.max_progress", 72);

        input.child("inventory").ifPresent(inventory::deserialize);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.size());
        for (int i = 0; i < inventory.size(); i++) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, i);
            inv.setItem(i, new ItemStack(itemAccess.getResource().getItem(), itemAccess.getAmount()));
        }
        assert this.level != null; // Is needed
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void tick(Level level, BlockPos pos, BlockState state, RefinedFurnaceBlockEntity entity) {
        ServerLevel serverLevel = (ServerLevel) level;
        ItemStack ingredient = inventory.getResource(INPUT_SLOT).toStack();
        ItemStack fuel = inventory.getResource(FUEL_SLOT).toStack();
        ItemStack result = inventory.getResource(OUTPUT_SLOT).toStack();
        this.heatLogic.conduction(level.getBlockState(worldPosition).getValue(LIT));
        boolean isLit;

        /*System.out.println("Progress:" +  progress + "/" + maxProgress);
        System.out.println("Lit:" +  litTimeRemaining + "/" + totalLitTime);*/

        if (litTimeRemaining > 0) {
            isLit = true;
            --litTimeRemaining;
        } else {
            isLit = false;
        }

        SingleRecipeInput input = new SingleRecipeInput(ingredient);
        Optional<RecipeHolder<SmeltingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, serverLevel);

        if (recipe.isEmpty()) {
            resetProgress();
            if (isLit) {
                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            } else {
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            }
        } else {
            ItemStack burnResult = recipe.get().value().assemble(new SingleRecipeInput(ingredient));
            int maxStackSize = burnResult.getMaxStackSize();

            if (isLit) {
                increaseCraftingProgress();

                if (hasCraftingFinished()) {
                    resetProgress();
                    craftItem();
                    bonusTracking();
                }
            }
            if (!isLit && canStartSmelting(result, maxStackSize, burnResult) && hasFuel()) {
                consumeFuel(inventory, fuel);
                totalLitTime = entity.getBurnDuration(level.fuelValues(), fuel);
                litTimeRemaining = totalLitTime;
                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            } else if (!isLit && !hasFuel()) {
                resetProgress();
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            }
        }
    }

    private static void consumeFuel(ItemStacksResourceHandler inventory, ItemStack fuel) {
        try(Transaction transaction = Transaction.openRoot()) {
            inventory.extract(FUEL_SLOT, inventory.getResource(FUEL_SLOT), 1, transaction);
            transaction.commit();
        }
    }

    private static boolean canStartSmelting(ItemStack result, int maxStackSize, ItemStack burnResult) {
        if (result.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(result, burnResult) ) {
            return false;
        } else {
            int resultCount = result.getCount() + burnResult.count();
            int maxResultCount = Math.min(maxStackSize, burnResult.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return itemStack.getBurnTime(RecipeType.SMELTING, fuelValues);
    }

    private void craftItem() {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(inventory.getResource(INPUT_SLOT).toStack()));
        /*
        WE WANT TO GO FROM +1 EXTRA EVERY 10 ITEMS TO +1 EVERY 2 ITEMS (maybe) BASED ON HEAT

        <1k HEAT = 10:1 => 1k HEAT WILL NEED 10 SMELTS
        <2k HEAT = 09:1
        <3k HEAT = 08:1
        <4k HEAT = 07:1
        <5k HEAT = 06:1
        <6k HEAT = 05:1
        <7k HEAT = 04:1
        <8k HEAT = 03:1
        <9k HEAT = 02:1
         */

        int extraBonus;
        if (bonusTracking()) {
            extraBonus = 1;
        } else {extraBonus = 0;}

        try(Transaction transaction = Transaction.openRoot()) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, OUTPUT_SLOT);

            inventory.extract(INPUT_SLOT, inventory.getResource(INPUT_SLOT), 1, transaction);
            inventory.set(OUTPUT_SLOT, ItemResource.of(output), itemAccess.getAmount() + output.getCount() + extraBonus);

            transaction.commit();
        }
    }

    private boolean bonusTracking() {
        int currentHeatInThousands = this.heatLogic.getTotalHeat() / 1000;
        int currentThreashold = 11 - currentHeatInThousands;
        if (bonusCounter >= currentThreashold) {
            bonusCounter = 0;
            System.out.println(bonusCounter + "/" + currentThreashold);
            return true;
        } else {
            bonusCounter++;
            System.out.println(bonusCounter + "/" + currentThreashold);
            return false;
        }
    }

    private boolean hasFuel() {
        return !inventory.getResource(FUEL_SLOT).isEmpty();
    }

    private Optional<RecipeHolder<SmeltingRecipe>> getCurrentRecipe() {
        if (!(level instanceof ServerLevel serverLevel)) return Optional.empty();
        return serverLevel.recipeAccess()
                .getRecipeFor(RecipeType.SMELTING,
                        new SingleRecipeInput(inventory.getResource(INPUT_SLOT).toStack()), level);
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 60;
    }

    /* BLOCK ENTITY SYNC */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public void onDataPacket(@NonNull Connection net, @NonNull ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
    }
}