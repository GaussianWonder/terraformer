package com.gaussianwonder.terraformer.capabilities;

import com.gaussianwonder.terraformer.api.capabilities.storage.IMatterStorage;
import com.gaussianwonder.terraformer.capabilities.storage.MatterStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMatter
{
    @CapabilityInject(IMatterStorage.class)
    public static Capability<IMatterStorage> MATTER = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IMatterStorage.class, new Capability.IStorage<IMatterStorage>() {
                    @Override
                    public INBT writeNBT(Capability<IMatterStorage> capability, IMatterStorage instance, Direction side)
                    {
                        CompoundNBT tag = new CompoundNBT();
                        IMatterStorage.Matter storedMatter = instance.getMatterStored();

                        tag.putFloat("solid_matter_stored", storedMatter.solid);
                        tag.putFloat("soft_matter_stored", storedMatter.soft);
                        tag.putFloat("granular_matter_stored", storedMatter.granular);

                        tag.putFloat("matter_capacity", instance.getMaxMatterStored());
                        tag.putFloat("matter_receive", instance.getMaxReceived());
                        tag.putFloat("matter_extract", instance.getMaxExtract());

                        return tag;
                    }

                    @Override
                    public void readNBT(Capability<IMatterStorage> capability, IMatterStorage instance, Direction side, INBT nbt)
                    {
                        if (!(instance instanceof MatterStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

                        instance.setMatter(new IMatterStorage.Matter(
                                ((CompoundNBT) nbt).getFloat("solid_matter_stored"),
                                ((CompoundNBT) nbt).getFloat("soft_matter_stored"),
                                ((CompoundNBT) nbt).getFloat("granular_matter_stored")
                        ));
                        instance.setMaxMatterStored(((CompoundNBT) nbt).getFloat("matter_capacity"));
                        instance.setMaxReceived(((CompoundNBT) nbt).getFloat("matter_receive"));
                        instance.setMaxExtract(((CompoundNBT) nbt).getFloat("matter_extract"));
                    }
                },
                () -> new MatterStorage(0)
        );
    }
}
