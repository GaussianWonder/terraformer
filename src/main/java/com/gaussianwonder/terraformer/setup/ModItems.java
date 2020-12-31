package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.items.BaseItem;
import net.minecraftforge.fml.RegistryObject;

//TODO Adapt to a proper BaseItem
public class ModItems {
    public static final RegistryObject<BaseItem> TERRAFORM_ROD = RegistryHandler.ITEMS.register("terraform_rod", BaseItem::new);

    static void register() { }
}
