package com.mahem.mahems_furnaces.data_gen;

import com.mahem.mahems_furnaces.FurnaceMod;
import com.mahem.mahems_furnaces.blocks.LavaFurnaceBlock;
import com.mahem.mahems_furnaces.mod_types.ModBlockType;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class ModModelProvider extends ModelProvider  {

    public ModModelProvider(PackOutput output) {
        super(output, FurnaceMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        List<Block> furnaces = List.of(
                ModBlockType.FORGE_FURNACE.get(),
                ModBlockType.REFINED_FURNACE.get(),
                ModBlockType.PRESSURISED_FURNACE.get(),
                ModBlockType.LAVA_FURNACE.get()
        );

        PropertyDispatch<VariantMutator> HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                .select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
                .select(Direction.NORTH, BlockModelGenerators.NOP);

        for (Block furnace : furnaces){
            if (furnace == ModBlockType.LAVA_FURNACE.get()) {
                Identifier offModel = ModelLocationUtils.getModelLocation(furnace, "");
                Identifier litModel = ModelLocationUtils.getModelLocation(furnace, "_lit");
                Identifier afterburnModel = ModelLocationUtils.getModelLocation(furnace, "_afterburn");

                MultiVariant offVariant = BlockModelGenerators.plainVariant(offModel);
                MultiVariant litVariant = BlockModelGenerators.plainVariant(litModel);
                MultiVariant afterburningVariant = BlockModelGenerators.plainVariant(afterburnModel);

                blockModels.blockStateOutput.accept(
                        MultiVariantGenerator.dispatch(furnace)
                                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT,LavaFurnaceBlock.AFTERBURNING)
                                        .select(Direction.NORTH, false, false, offVariant)
                                        .select(Direction.EAST,  false, false, offVariant.with(BlockModelGenerators.Y_ROT_90))
                                        .select(Direction.SOUTH, false, false, offVariant.with(BlockModelGenerators.Y_ROT_180))
                                        .select(Direction.WEST,  false, false, offVariant.with(BlockModelGenerators.Y_ROT_270))

                                        .select(Direction.NORTH, true, false, litVariant)
                                        .select(Direction.EAST,  true, false, litVariant.with(BlockModelGenerators.Y_ROT_90))
                                        .select(Direction.SOUTH, true, false, litVariant.with(BlockModelGenerators.Y_ROT_180))
                                        .select(Direction.WEST,  true, false, litVariant.with(BlockModelGenerators.Y_ROT_270))

                                        .select(Direction.NORTH, true, true, afterburningVariant)
                                        .select(Direction.EAST,  true, true, afterburningVariant.with(BlockModelGenerators.Y_ROT_90))
                                        .select(Direction.SOUTH, true, true, afterburningVariant.with(BlockModelGenerators.Y_ROT_180))
                                        .select(Direction.WEST,  true, true, afterburningVariant.with(BlockModelGenerators.Y_ROT_270))

                                        // Feels unclean but I guess it wants this too
                                        .select(Direction.NORTH, false, true, offVariant)
                                        .select(Direction.EAST,  false, true, offVariant.with(BlockModelGenerators.Y_ROT_90))
                                        .select(Direction.SOUTH, false, true, offVariant.with(BlockModelGenerators.Y_ROT_180))
                                        .select(Direction.WEST,  false, true, offVariant.with(BlockModelGenerators.Y_ROT_270)))
                );
                continue;
            }

            Identifier offModel = ModelLocationUtils.getModelLocation(furnace, "");
            Identifier litModel = ModelLocationUtils.getModelLocation(furnace, "_lit");

            MultiVariant offVariant = BlockModelGenerators.plainVariant(offModel);
            MultiVariant litVariant = BlockModelGenerators.plainVariant(litModel);

            blockModels.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(furnace)
                            .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT)

                                    .select(Direction.NORTH, false, offVariant)
                                    .select(Direction.EAST, false, offVariant.with(BlockModelGenerators.Y_ROT_90))
                                    .select(Direction.SOUTH, false, offVariant.with(BlockModelGenerators.Y_ROT_180))
                                    .select(Direction.WEST, false, offVariant.with(BlockModelGenerators.Y_ROT_270))

                                    .select(Direction.NORTH, true, litVariant)
                                    .select(Direction.EAST, true, litVariant.with(BlockModelGenerators.Y_ROT_90))
                                    .select(Direction.SOUTH, true, litVariant.with(BlockModelGenerators.Y_ROT_180))
                                    .select(Direction.WEST, true, litVariant.with(BlockModelGenerators.Y_ROT_270)))
            );
        }
    }
}
