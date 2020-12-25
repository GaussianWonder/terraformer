package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.BaseBlock;
import com.gaussianwonder.terraformer.setup.blocks.BaseBlockItem;
import com.gaussianwonder.terraformer.setup.blocks.ModBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {
    public static final ModBlock<BaseBlock, BaseBlockItem> TEST_BLOCK = new ModBlock<>("test_block",
            () -> new BaseBlock(AbstractBlock.Properties
                    .create(Material.ROCK)
                    .hardnessAndResistance(3, 10)
                    .sound(SoundType.CHAIN)));

    static void register() { }
}
