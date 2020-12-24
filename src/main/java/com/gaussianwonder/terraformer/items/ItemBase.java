package com.gaussianwonder.terraformer.items;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.item.Item;

//TODO finish this ASAP together with BaseBlock
public class ItemBase extends Item {
    public ItemBase() {
        super(new Item.Properties().group(TerraformerMod.ITEM_GROUP));
    }

    public ItemBase(Item.Properties properties) {
        super(properties.group(TerraformerMod.ITEM_GROUP)); // every item of this mod should always be part of the custom group
    }
}
