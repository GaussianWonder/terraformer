package com.gaussianwonder.terraformer.setup.items;

import com.gaussianwonder.terraformer.setup.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModItem<ItemType extends BaseItem> {
    public RegistryObject<ItemType> item;

    public ModItem(String name, final Supplier<? extends ItemType> itemSupplier) {
        item = RegistryHandler.ITEMS.register(name, itemSupplier);
    }

    public ItemType get() {
        return item.get();
    }
}
