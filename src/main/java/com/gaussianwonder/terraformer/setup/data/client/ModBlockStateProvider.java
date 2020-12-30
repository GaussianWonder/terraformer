package com.gaussianwonder.terraformer.setup.data.client;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, TerraformerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //simpleBlock(ModBlocks.TEST_BLOCK.get());
    }
}
