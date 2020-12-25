package com.gaussianwonder.terraformer.setup.items;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.item.Item;

public class BaseItem extends Item {
    public BaseItem() {
        super(new Item.Properties().group(TerraformerMod.ITEM_GROUP));
    }

    public BaseItem(Item.Properties properties) {
        super(properties.group(TerraformerMod.ITEM_GROUP)); // any item of this mod should always be part of the custom group
    }
}