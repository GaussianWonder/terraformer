package com.gaussianwonder.terraformer.data.client;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TerraformerMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        defaultBuilder(itemGenerated, "speed_upgrade");
        defaultBuilder(itemGenerated, "speed_downgrade");
        defaultBuilder(itemGenerated, "output_upgrade");
        defaultBuilder(itemGenerated, "input_upgrade");

        defaultBuilder(itemGenerated, "terraform_rod");
    }

    private ItemModelBuilder defaultBuilder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
