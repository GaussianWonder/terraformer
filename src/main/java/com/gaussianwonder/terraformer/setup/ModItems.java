package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.items.BaseItem;
import com.gaussianwonder.terraformer.setup.items.UpgradeItem;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    // Machine upgrades
    public static final RegistryObject<UpgradeItem> SPEED_UPGRADE = RegistryHandler.ITEMS.register("speed_upgrade", UpgradeItem::new);
    public static final RegistryObject<UpgradeItem> SPEED_DOWNGRADE = RegistryHandler.ITEMS.register("speed_downgrade", UpgradeItem::new);
    public static final RegistryObject<UpgradeItem> OUTPUT_UPGRADE = RegistryHandler.ITEMS.register("output_upgrade", UpgradeItem::new);
    public static final RegistryObject<UpgradeItem> INPUT_UPGRADE = RegistryHandler.ITEMS.register("input_upgrade", UpgradeItem::new);

    public static final RegistryObject<BaseItem> TERRAFORM_ROD = RegistryHandler.ITEMS.register("terraform_rod", BaseItem::new);

    static void register() { }
}
