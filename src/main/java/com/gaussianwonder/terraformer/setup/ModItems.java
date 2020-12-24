package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.items.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

//TODO Adapt to a proper BaseItem
public class ModItems {
    public static final RegistryObject<Item> TERRAFORM_ROD = RegistryHandler.ITEMS.register("terraform_rod", ItemBase::new);

    static void register() {

    }
}
