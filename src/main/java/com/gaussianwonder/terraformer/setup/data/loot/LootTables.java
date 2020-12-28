package com.gaussianwonder.terraformer.setup.data.loot;

import com.gaussianwonder.terraformer.setup.ModBlocks;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {
    public LootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void addTables() {
        addStandardTable(ModBlocks.TEST_BLOCK);
    }
}