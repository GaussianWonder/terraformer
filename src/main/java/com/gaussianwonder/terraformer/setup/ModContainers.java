package com.gaussianwonder.terraformer.setup;

import com.gaussianwonder.terraformer.setup.blocks.containers.MatterRecyclerContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    public static final RegistryObject<ContainerType<MatterRecyclerContainer>> MATTER_RECYCLER_CONTAINER = RegistryHandler.CONTAINERS.register(
            "matter_recycler",
            () -> IForgeContainerType.create((windowID, inv, data) -> {
                BlockPos blockPos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new MatterRecyclerContainer(windowID, world, blockPos, inv, inv.player);
            })
    );


    public static void register() { }
}
