package com.mahem.furnace_mod.data_gen;

import com.mahem.furnace_mod.FurnaceMod;
import com.mahem.furnace_mod.ModBlockTypes;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.ClientAsset;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, FurnaceMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        blockModels.createHorizontallyRotatedBlock(ModBlockTypes.FORGE_FURNACE.get(), TexturedModel.ORIENTABLE);



    }
}
