package com.gaussianwonder.terraformer.setup.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class BaseBlock extends Block {
    protected static String name = "test_block";

    public BaseBlock(AbstractBlock.Properties properties) { // Almost each block has distinct properties
        super(properties); // I couldn't think of any default property yet
    }

    public String getName() {
        return name;
    }
}