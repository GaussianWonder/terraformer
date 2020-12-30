package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blockitems.BaseBlockItem;
import com.gaussianwonder.terraformer.setup.blocks.MatterRecyclerBlock;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
    public static final RegistryObject<MatterRecyclerBlock> MATTER_RECYCLER = RegistryHandler.BLOCKS.register(
            "matter_recycler",
            MatterRecyclerBlock::new
    );
    public static final RegistryObject<Item> MATTER_RECYCLER_ITEM = RegistryHandler.ITEMS.register(
            "matter_recycler",
            () -> new BaseBlockItem(MATTER_RECYCLER.get())
    );

    static void register() { }
}
