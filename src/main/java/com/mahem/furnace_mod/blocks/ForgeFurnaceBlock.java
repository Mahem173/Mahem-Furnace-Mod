package com.mahem.furnace_mod.blocks;

import com.mahem.furnace_mod.mod_types.ModBlockEntityType;
import com.mahem.furnace_mod.block_entities.ForgeFurnaceBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.NonNull;

public class ForgeFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<com.mahem.furnace_mod.blocks.ForgeFurnaceBlock> CODEC = simpleCodec(com.mahem.furnace_mod.blocks.ForgeFurnaceBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    @Override
    public MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    public ForgeFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new ForgeFurnaceBlockEntity(worldPosition, blockState); // Should be always non-null?
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> propertyBuilder) {
        propertyBuilder.add(FACING);
        propertyBuilder.add(BlockStateProperties.LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext propertyContext) {
        // 4. Set the direction based on where the player is looking when they place it
        return this.defaultBlockState().setValue(FACING, propertyContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        if (type == ModBlockEntityType.FORGE_FURNACE_ENTITY.get()) {
            return (BlockEntityTicker<T>) (level1, pos, state1, blockEntity) -> ForgeFurnaceBlockEntity.tick(level1, pos, state1, (ForgeFurnaceBlockEntity) blockEntity);
        }
        return null;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ForgeFurnaceBlockEntity) {
            player.openMenu((MenuProvider)blockEntity);
            player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double r = 0.52;
            double ss = random.nextDouble() * 0.6 - 0.3;
            double dx = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : ss;
            double dy = random.nextDouble() * 9.0 / 16.0;
            double dz = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : ss;
            level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
        }
    }
}