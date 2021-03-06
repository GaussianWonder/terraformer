package com.gaussianwonder.terraformer.data.tags;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.setup.ModItems;
import com.gaussianwonder.terraformer.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, TerraformerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        copy(ModTags.Blocks.MATTER_UTILS, ModTags.Items.MATTER_UTILS);

        getOrCreateBuilder(ModTags.Items.UPGRADES_MACHINE).add(ModItems.SPEED_UPGRADE.get());
        getOrCreateBuilder(ModTags.Items.UPGRADES_MACHINE).add(ModItems.SPEED_DOWNGRADE.get());
        getOrCreateBuilder(ModTags.Items.UPGRADES_MACHINE).add(ModItems.OUTPUT_UPGRADE.get());
        getOrCreateBuilder(ModTags.Items.UPGRADES_MACHINE).add(ModItems.INPUT_UPGRADE.get());

        getOrCreateBuilder(ModTags.Items.RODS_TERRAFORM).add(ModItems.TERRAFORM_ROD.get());
    }
}
