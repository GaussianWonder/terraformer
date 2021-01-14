package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.items.BaseBlockItem;
import com.gaussianwonder.terraformer.setup.blocks.MatterFuserBlock;
import com.gaussianwonder.terraformer.setup.blocks.MatterRecyclerBlock;
import com.gaussianwonder.terraformer.setup.blocks.MatterStorageBlock;
import net.minecraft.item.Item;
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
    public static final RegistryObject<MatterStorageBlock> MATTER_STORAGE = RegistryHandler.BLOCKS.register(
            "matter_storage",
            MatterStorageBlock::new
    );

    // Block Items
    public static final RegistryObject<BaseBlockItem> MATTER_RECYCLER_ITEM = RegistryHandler.ITEMS.register(
            "matter_recycler",
            () -> new BaseBlockItem(MATTER_RECYCLER.get(), new Item.Properties().maxStackSize(1))
    );
    public static final RegistryObject<BaseBlockItem> MATTER_FUSER_ITEM = RegistryHandler.ITEMS.register(
            "matter_fuser",
            () -> new BaseBlockItem(MATTER_FUSER.get(), new Item.Properties().maxStackSize(1))
    );
    public static final RegistryObject<BaseBlockItem> MATTER_STORAGE_ITEM = RegistryHandler.ITEMS.register(
            "matter_storage",
            () -> new BaseBlockItem(MATTER_STORAGE.get(), new Item.Properties().maxStackSize(1))
    );

    static void register() { }
}
