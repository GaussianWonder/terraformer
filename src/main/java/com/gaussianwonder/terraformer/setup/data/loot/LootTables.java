package com.gaussianwonder.terraformer.setup.data.loot;

import com.gaussianwonder.terraformer.setup.ModBlocks;
import com.gaussianwonder.terraformer.setup.blocks.ModBlock;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {
    public LootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    public void addStandardTable(ModBlock block) {
        lootTables.put(block.get(), createStandardTable(block.name, block.get()));
    }

    @Override
    protected void addTables() {
//        lootTables.put(ModBlocks.TEST_BLOCK.get(), createStandardTable("test_block", ModBlocks.TEST_BLOCK.get()));
        addStandardTable(ModBlocks.TEST_BLOCK);
    }
}