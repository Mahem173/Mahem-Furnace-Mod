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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class ForgeFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<com.mahem.furnace_mod.blocks.ForgeFurnaceBlock> CODEC = simpleCodec(com.mahem.furnace_mod.blocks.ForgeFurnaceBlock::new);

    @Override
    public MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    public ForgeFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new ForgeFurnaceBlockEntity(worldPosition, blockState); // Should be always non-null?
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