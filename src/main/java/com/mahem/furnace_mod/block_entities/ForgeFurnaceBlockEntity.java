package com.mahem.furnace_mod.block_entities;

import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import com.mahem.furnace_mod.recipes.ForgeFurnaceRecipe;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
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

import static com.mahem.furnace_mod.blocks.ForgeFurnaceBlock.LIT;
import static com.mahem.furnace_mod.mod_types.ModBlockEntityType.FORGE_FURNACE_ENTITY;

public class ForgeFurnaceBlockEntity extends BaseContainerBlockEntity {
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(3) {
        @Override
        protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            ForgeFurnaceBlockEntity.this.setChanged();
        }
    };

    private final int SIZE = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    private int progress = 0;
    private int maxProgress = 200;
    private int litTimeRemaining = 0;
    private int totalLitTime = 0;
    private boolean isCrafting = false;

    private final HeatLogic heatLogic = new HeatLogic();

    public ForgeFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(FORGE_FURNACE_ENTITY.get(), worldPosition, blockState);
        this.heatLogic.setAddHeat(10);
        ContainerData data = new ContainerData() {
            @Override
            public int get(int dataId) {
                return switch (dataId) {
                    case 0 -> ForgeFurnaceBlockEntity.this.progress;
                    case 1 -> ForgeFurnaceBlockEntity.this.maxProgress;
                    case 2 -> ForgeFurnaceBlockEntity.this.litTimeRemaining;
                    case 3 -> ForgeFurnaceBlockEntity.this.totalLitTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int dataId, int value) {
                switch (dataId) {
                    case 0:
                        ForgeFurnaceBlockEntity.this.progress = value;
                    case 1:
                        ForgeFurnaceBlockEntity.this.maxProgress = value;
                    case 2:
                        ForgeFurnaceBlockEntity.this.litTimeRemaining = value;
                    case 3:
                        ForgeFurnaceBlockEntity.this.totalLitTime = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NonNull Component getDefaultName() {
        return Component.translatable("block.furnace_mod.forge_furnace");
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
        return new ForgeFurnaceMenu(containerId, inventory, this);
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

    public void tick(Level level, BlockPos pos, BlockState state, ForgeFurnaceBlockEntity entity) {
        ServerLevel serverLevel = (ServerLevel) level;
        ItemStack fuel = inventory.getResource(FUEL_SLOT).toStack();
        ItemStack ingredient = inventory.getResource(INPUT_SLOT).toStack();

        boolean isLit = this.heatLogic.conduction(level.getBlockState(worldPosition).getValue(LIT));

        if (!hasRecipe()) { // Here we must have recipe ready
            System.out.println("its joever");
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            isCrafting = false;
            return;
        }

        SingleRecipeInput input = new SingleRecipeInput(ingredient);
        Optional<RecipeHolder<SmeltingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, serverLevel);
        if (recipe.isEmpty()) {
            System.out.println("no smelting recipe");
            return;
        }

        ItemStack burnResult = recipe.get().value().assemble(new SingleRecipeInput(ingredient));
        int maxStackSize = burnResult.getMaxStackSize();

        /* CURRENT PROBLEMS
        1. NO ARROW OR FIRE IN SCREEN
        2. CAN SMELT WITHOUT FUEL, OVERFLOWS INDEFINITELY UNTIL FUEL IS PUT WHICH RESETS PROGRESS
        3. CAN SMELT ONLY FOR "ONE" BURN CYCLE AFTERWARDS IT GIVES UP
        */

        if (canBurn(entity.items, maxStackSize, burnResult)) {
            System.out.println("Burnable !!!!!");
        }

        // Start Smelting
        if (hasRecipe() && canBurn(entity.items, maxStackSize, burnResult) && isOutputSlotEmptyOrReceivable()) {
            burn(entity.items, ingredient, fuel);
            consumeFuel(entity.items, fuel);
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            increaseCraftingProgress();
            isCrafting = true;
            System.out.println("Im smelting");
        }

        // Continue Smelting
        else if (isLit && isCrafting) {  // Probably will need to be moved to be before starting to smelt
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            increaseCraftingProgress();
            isCrafting = true;

            // Finish Crafting
            if (hasCraftingFinished()) {
                resetProgress();
                craftItem();
            }
        }
        else {
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            isCrafting = false;
        }

        System.out.println("Progress:" +  progress + "/" + maxProgress);


    }

    private static void consumeFuel(NonNullList<ItemStack> items, ItemStack fuel) {
        Item fuelItem = fuel.getItem();
        ItemStackTemplate remainder = fuel.getCraftingRemainder();
        fuel.shrink(1);
        if (fuel.isEmpty()) {
            items.set(1, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }
    }

    private static boolean canBurn(NonNullList<ItemStack> items, int maxStackSize, ItemStack burnResult) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult)) {
            return false;
        } else {
            int resultCount = resultItemStack.getCount() + burnResult.count();
            int maxResultCount = Math.min(maxStackSize, burnResult.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    private static void burn(NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            items.set(2, result.copy());
        } else {
            resultItemStack.grow(result.getCount());
        }

        if (inputItemStack.is(Items.WET_SPONGE) && !items.get(1).isEmpty() && items.get(1).is(Items.BUCKET)) {
            items.set(1, new ItemStack(Items.WATER_BUCKET));
        }

        inputItemStack.shrink(1);
    }


    private void craftItem() {
        Optional<RecipeHolder<SmeltingRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().assemble(new SingleRecipeInput(inventory.getResource(INPUT_SLOT).toStack()));

        try(Transaction transaction = Transaction.openRoot()) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(inventory, OUTPUT_SLOT);

            inventory.extract(INPUT_SLOT, inventory.getResource(INPUT_SLOT), 1, transaction);
            inventory.set(OUTPUT_SLOT, ItemResource.of(output), itemAccess.getAmount() + output.getCount());

            transaction.commit();
        }
    }

    private boolean hasRecipe() {
        return getCurrentRecipe().isPresent();
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

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return inventory.getResource(OUTPUT_SLOT).isEmpty() ||
                inventory.getResource(OUTPUT_SLOT).is(output.getItem());
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = inventory.getResource(OUTPUT_SLOT).isEmpty() ? 64 : inventory.getResource(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = inventory.getAmountAsInt(OUTPUT_SLOT);

        return maxCount >= currentCount + count;
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return inventory.getResource(OUTPUT_SLOT).isEmpty() ||
                inventory.getResource(OUTPUT_SLOT).test(stack -> stack.count() < stack.getMaxStackSize());
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 200;
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