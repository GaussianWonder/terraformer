package com.gaussianwonder.terraformer.setup.items;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UpgradeItem extends BaseItem{
    public UpgradeItem() {
        super(new Item.Properties().group(TerraformerMod.ITEM_GROUP));
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 32; // this is going to trigger people
    }
}
