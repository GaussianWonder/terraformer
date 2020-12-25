package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.items.BaseItem;
import com.gaussianwonder.terraformer.setup.items.ModItem;

//TODO Adapt to a proper BaseItem
public class ModItems {
    public static final ModItem<BaseItem> TERRAFORM_ROD = new ModItem<>("terraform_rod", BaseItem::new);

    static void register() { }
}
