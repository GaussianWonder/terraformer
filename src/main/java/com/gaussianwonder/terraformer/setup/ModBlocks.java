package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.block_items.MatterFuserBlockItem;
import com.gaussianwonder.terraformer.setup.block_items.MatterRecyclerBlockItem;
import com.gaussianwonder.terraformer.setup.blocks.MatterFuserBlock;
import com.gaussianwonder.terraformer.setup.blocks.MatterRecyclerBlock;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
    // Blocks
    public static final RegistryObject<MatterRecyclerBlock> MATTER_RECYCLER = RegistryHandler.BLOCKS.register(
            "matter_recycler",
            MatterRecyclerBlock::new
    );
    public static final RegistryObject<MatterFuserBlock> MATTER_FUSER = RegistryHandler.BLOCKS.register(
            "matter_fuser",
            MatterFuserBlock::new
    );

    // Block Items
    public static final RegistryObject<MatterRecyclerBlockItem> MATTER_RECYCLER_ITEM = RegistryHandler.ITEMS.register(
            "matter_recycler",
            MatterRecyclerBlockItem::new
    );
    public static final RegistryObject<MatterFuserBlockItem> MATTER_FUSER_ITEM = RegistryHandler.ITEMS.register(
            "matter_fuser",
            MatterFuserBlockItem::new
    );

    static void register() { }
}
