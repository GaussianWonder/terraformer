package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.items.BaseItem;
import net.minecraftforge.fml.RegistryObject;

//TODO Adapt to a proper BaseItem
public class ModItems {
    // Machine upgrades
    public static final RegistryObject<BaseItem> SPEED_UPGRADE = RegistryHandler.ITEMS.register("speed_upgrade", BaseItem::new);
    public static final RegistryObject<BaseItem> SPEED_DOWNGRADE = RegistryHandler.ITEMS.register("speed_downgrade", BaseItem::new);
    public static final RegistryObject<BaseItem> OUTPUT_UPGRADE = RegistryHandler.ITEMS.register("output_upgrade", BaseItem::new);
    public static final RegistryObject<BaseItem> INPUT_UPGRADE = RegistryHandler.ITEMS.register("input_upgrade", BaseItem::new);

    public static final RegistryObject<BaseItem> TERRAFORM_ROD = RegistryHandler.ITEMS.register("terraform_rod", BaseItem::new);

    static void register() { }
}
