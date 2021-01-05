package com.gaussianwonder.terraformer.setup.block_items;

import com.gaussianwonder.terraformer.setup.ModBlocks;
import net.minecraft.item.Item;

public class MatterRecyclerBlockItem extends BaseBlockItem {
    public MatterRecyclerBlockItem() {
        super(ModBlocks.MATTER_RECYCLER.get(), new Item.Properties().maxStackSize(1));
    }

    @Override
    public boolean shouldSyncTag() {
        return true; // it defaults to true either way, but i'll keep this here in case something changes later on
    }
}
