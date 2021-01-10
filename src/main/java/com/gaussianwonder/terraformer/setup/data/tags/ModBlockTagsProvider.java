package com.gaussianwonder.terraformer.setup.data.tags;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.ModBlocks;
import com.gaussianwonder.terraformer.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, TerraformerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ModTags.Blocks.MATTER_UTILS).add(ModBlocks.MATTER_RECYCLER.get());
        getOrCreateBuilder(ModTags.Blocks.MATTER_UTILS).add(ModBlocks.MATTER_FUSER.get());
        getOrCreateBuilder(ModTags.Blocks.MATTER_UTILS).add(ModBlocks.MATTER_STORAGE.get());
    }
}
