package com.gaussianwonder.terraformer.setup;

import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * If the Item does not have the Recyclable capability this is the next place to search for it
 */
public class MatterDictionary {
    public static HashMap<Item, Float> matterDictionary = new HashMap<Item, Float>();

    public static void register() { }
}
