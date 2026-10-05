package com.mahem.mahems_furnaces.block_entities;

import com.mahem.mahems_furnaces.internal_logic.HeatLogic;
import com.mahem.mahems_furnaces.menus.LavaFurnaceMenu;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.mahem.mahems_furnaces.blocks.LavaFurnaceBlock.AFTERBURNING;
import static com.mahem.mahems_furnaces.blocks.LavaFurnaceBlock.LIT;
import static com.mahem.mahems_furnaces.mod_types.ModBlockEntityType.LAVA_FURNACE_ENTITY;
import static net.minecraft.world.item.Items.*;

public class LavaFurnaceBlockEntity extends BaseContainerBlockEntity {
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(3) {
        @Override
        protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            LavaFurnaceBlockEntity.this.setChanged();
        }
    };


    private final int SIZE = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 2000;
    private int litTimeRemaining = 0;
    private int totalLitTime = 1000000;
    private int afterburnTimeRemaining = 0;
    private int totalAfterburnTime = 200000;

    private final HeatLogic heatLogic = new HeatLogic();

    public LavaFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(LAVA_FURNACE_ENTITY.get(), worldPosition, blockState);
        this.heatLogic.setHeatValue(12);
        this.heatLogic.setCeilHeat(20000);
        this.data = new ContainerData() {
            @Override
            public int get(int dataId) {
                return switch (dataId) {
                    case 0 -> LavaFurnaceBlockEntity.this.progress;
                    case 1 -> LavaFurnaceBlockEntity.this.maxProgress;
                    case 2 -> LavaFurnaceBlockEntity.this.litTimeRemaining;
                    case 3 -> LavaFurnaceBlockEntity.this.totalLitTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int dataId, int value) {
                switch (dataId) {
                    case 0:
                        LavaFurnaceBlockEntity.this.progress = value;
                        break;
                    case 1:
                        LavaFurnaceBlockEntity.this.maxProgress = value;
                        break;
                    case 2:
                        LavaFurnaceBlockEntity.this.litTimeRemaining = value;
                        break;
                    case 3:
                        LavaFurnaceBlockEntity.this.totalLitTime = value;
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
        return Component.translatable("block.mahems_furnaces.lava_furnace");
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
        return new LavaFurnaceMenu(containerId, inventory, this);
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

    public void tick(Level level, BlockPos pos, BlockState state, LavaFurnaceBlockEntity entity) {
        ServerLevel serverLevel = (ServerLevel) level;
        ItemStack ingredient = inventory.getResource(INPUT_SLOT).toStack();
        ItemStack fuel = inventory.getResource(FUEL_SLOT).toStack();
        ItemStack result = inventory.getResource(OUTPUT_SLOT).toStack();

        this.heatLogic.conduction(level.getBlockState(worldPosition).getValue(LIT));
        boolean isLit;
        int heatSubtractd;

        if (this.heatLogic.getTotalHeat() > 120000) {
            heatSubtractd = 8;
        } else {heatSubtractd = 10;}

        if (litTimeRemaining > 0) {
            isLit = true;
            litTimeRemaining -= heatSubtractd;
        } else {isLit = false;}

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
                increaseCraftingProgress(ingredient);

                System.out.println("Lit time remaining: " + (litTimeRemaining/10000) + "k");
                System.out.println("Afterburn time remaining: " + (afterburnTimeRemaining/10000) + "k");

                if (afterburnTimeRemaining > 0) {
                    afterburnTimeRemaining -= 10;
                    level.setBlockAndUpdate(pos, state.setValue(AFTERBURNING, true));
                } else {level.setBlockAndUpdate(pos, state.setValue(AFTERBURNING, false));}

                if (hasCraftingFinished()) {
                    resetProgress();
                    craftItem();
                }
            }

            /*
            FEW POINTS TO NOTE:

            - BASICALLY THIS FURNACE HAS BIG LAVA STORAGE SPACE (5 BUCKETS IN TOTAL) SO 1000k TICKS IN TOTAL
            - IF THIS STORAGE GET ABOVE THE 1000k LIMIT, REMAINING TICKS GO INTO AFTERBURNING, THESE TICKS ARE INDEPENDENT OF LIT TIME TICKS
            - SPEED IS DETERMINED BY AMOUNT OF LAVA AS WELL OF AFTERBURNING AS WELL IF THE ITEM HAS NETHER ORIGIN (INCLUDING ANCIENT DEBRIS)
            - CONSUMPTION OF LAVA IS REDUCED BY HIGHER HEAT

            WOULD BE COOL IF IT WAS SIMILAR TO RESPAWN ANCHOR TOP TEXTURE ANIMATION BUT TAKES TIME,
            A SORT OF RETEXTURE TO MORE MATCH RESPAWN ANCHOR
             */

            if (canStartSmelting(result, maxStackSize, burnResult) && hasFuel()) {
                consumeFuel(inventory);
                if (litTimeRemaining > 800000) {
                    level.playLocalSound(pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0f, 1.0f, false); // Why doesn't it work?
                    afterburnTimeRemaining = (litTimeRemaining + 200000) - totalLitTime;
                } else {litTimeRemaining += 200000;}

                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            }

            else if (!isLit && !hasFuel()) {
                resetProgress();
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                level.setBlockAndUpdate(pos, state.setValue(AFTERBURNING, false));
            }
        }
    }

    private static void consumeFuel(ItemStacksResourceHandler inventory) {
        try(Transaction transaction = Transaction.openRoot()) {
            inventory.extract(FUEL_SLOT, inventory.getResource(FUEL_SLOT), 1, transaction);
            inventory.set(FUEL_SLOT, ItemResource.of(BUCKET), 1);

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

    private boolean hasFuel() {
        return inventory.getResource(FUEL_SLOT) == ItemResource.of(LAVA_BUCKET);
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

    private void increaseCraftingProgress(ItemStack ingredient) {
        ItemResource itemResource = ItemResource.of(ingredient);
        int progressIncrement = 0;

        List<ItemResource> netherOrigin = List.of(
            ItemResource.of(ANCIENT_DEBRIS),
            ItemResource.of(NETHER_QUARTZ_ORE),
            ItemResource.of(NETHER_BRICKS),
            ItemResource.of(BASALT),
            ItemResource.of(POLISHED_BLACKSTONE_BRICKS),
            ItemResource.of(NETHERRACK)
        );

        // This may be written better?

        if (litTimeRemaining > 800000) {
            progressIncrement += 2;
        } else if (litTimeRemaining > 500000) {
            progressIncrement += 1;
        }

        if (afterburnTimeRemaining > 0) {
            progressIncrement += 3;
        }

        for (ItemResource item : netherOrigin) {
            if (item == itemResource) {
                progressIncrement += 5;
            }
        }

        System.out.println("Progress increment: " + progressIncrement);
        progress += 10 + progressIncrement;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 2000;
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