package com.gaussianwonder.terraformer.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    //TODO make my own ItemGroup
    public static final RegistryObject<Item> TERRAFORM_ROD = Registration.ITEMS.register("terraform_rod", () ->
            new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    static void register() {

    }
}
