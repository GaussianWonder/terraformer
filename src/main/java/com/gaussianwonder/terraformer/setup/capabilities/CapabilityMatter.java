package com.gaussianwonder.terraformer.setup.capabilities;

import com.gaussianwonder.terraformer.setup.capabilities.storage.IMatterStorage;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
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

                        tag.putFloat("matter_stored", instance.getMatterStored());
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

                        instance.setMatter(((CompoundNBT) nbt).getFloat("matter_stored"));
                        instance.setMaxMatterStored(((CompoundNBT) nbt).getFloat("matter_capacity"));
                        instance.setMaxReceived(((CompoundNBT) nbt).getFloat("matter_receive"));
                        instance.setMaxExtract(((CompoundNBT) nbt).getFloat("matter_extract"));
                    }
                },
                () -> new MatterStorage(1000)
        );
    }
}
