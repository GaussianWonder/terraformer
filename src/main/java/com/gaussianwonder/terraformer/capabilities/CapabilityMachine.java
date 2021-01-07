package com.gaussianwonder.terraformer.capabilities;

import com.gaussianwonder.terraformer.capabilities.handler.IMachineHandler;
import com.gaussianwonder.terraformer.capabilities.handler.MachineHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMachine {
    @CapabilityInject(IMachineHandler.class)
    public static Capability<IMachineHandler> MACHINE = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IMachineHandler.class, new Capability.IStorage<IMachineHandler>() {
                    @Override
                    public INBT writeNBT(Capability<IMachineHandler> capability, IMachineHandler instance, Direction side)
                    {
                        IMachineHandler.Stats baseStats = instance.getBaseStats();
                        IMachineHandler.Stats upgradedStats = instance.getUpgradedStats();

                        CompoundNBT tag = new CompoundNBT();

                        tag.putInt("cooldown", instance.getCooldown());

                        tag.putInt("base_speed", baseStats.speed);
                        tag.putInt("base_output", baseStats.output);
                        tag.putInt("base_input", baseStats.input);

                        tag.putInt("upgrade_speed", upgradedStats.speed);
                        tag.putInt("upgrade_output", upgradedStats.output);
                        tag.putInt("upgrade_input", upgradedStats.input);

                        return tag;
                    }

                    @Override
                    public void readNBT(Capability<IMachineHandler> capability, IMachineHandler instance, Direction side, INBT nbt)
                    {
                        if (!(instance instanceof MachineHandler))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");

                        instance.setCooldown(((CompoundNBT) nbt).getInt("cooldown"));

                        instance.setBaseStats(new IMachineHandler.Stats(
                                ((CompoundNBT) nbt).getInt("base_speed"),
                                ((CompoundNBT) nbt).getInt("base_output"),
                                ((CompoundNBT) nbt).getInt("base_input")
                        ));

                        instance.setUpgradedStats(new IMachineHandler.Stats(
                                ((CompoundNBT) nbt).getInt("upgrade_speed"),
                                ((CompoundNBT) nbt).getInt("upgrade_output"),
                                ((CompoundNBT) nbt).getInt("upgrade_input")
                        ));
                    }
                },
                () -> new MachineHandler()
        );
    }
}
