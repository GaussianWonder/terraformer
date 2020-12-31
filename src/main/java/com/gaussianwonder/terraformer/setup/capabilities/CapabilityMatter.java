package com.gaussianwonder.terraformer.setup.capabilities;

import com.gaussianwonder.terraformer.setup.capabilities.i_storage.IMatterStorage;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
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
                        tag.putFloat("matter", instance.getMatterStored());

                        return tag;
                        // return FloatNBT.valueOf(instance.getMatterStored());
                    }

                    @Override
                    public void readNBT(Capability<IMatterStorage> capability, IMatterStorage instance, Direction side, INBT nbt)
                    {
                        if (!(instance instanceof MatterStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                         ((MatterStorage)instance).setMatter(((CompoundNBT) nbt).getFloat("matter"));
                         // .setMatter( ((FloatNBT)nbt).getFloat() )
                    }
                },
                () -> new MatterStorage(1000)
        );
    }
}
