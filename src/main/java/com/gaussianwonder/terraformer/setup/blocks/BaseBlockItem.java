package com.gaussianwonder.terraformer.setup.blocks;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;;

public class BaseBlockItem extends BlockItem { // similar to BaseItem
    public BaseBlockItem(Block block, Item.Properties properties) {
        super(block, properties.group(TerraformerMod.ITEM_GROUP));
    }

    public BaseBlockItem(Block block) {
        super(block, new Item.Properties().group(TerraformerMod.ITEM_GROUP));
    }
}