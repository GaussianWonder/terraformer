package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.block_items.MatterRecyclerBlockItem;
import com.gaussianwonder.terraformer.setup.blocks.MatterRecyclerBlock;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<MatterRecyclerBlock> MATTER_RECYCLER = RegistryHandler.BLOCKS.register(
            "matter_recycler",
            MatterRecyclerBlock::new
    );
    public static final RegistryObject<MatterRecyclerBlockItem> MATTER_RECYCLER_ITEM = RegistryHandler.ITEMS.register(
            "matter_recycler",
            MatterRecyclerBlockItem::new
    );

    static void register() { }
}
