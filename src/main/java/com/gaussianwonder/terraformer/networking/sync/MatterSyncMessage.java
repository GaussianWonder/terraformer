package com.gaussianwonder.terraformer.networking.sync;

import com.gaussianwonder.terraformer.networking.PacketHandler;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTitle;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MatterSyncMessage {
    private BlockPos blockPos;
    private boolean failed;

    MatterSyncMessage(boolean failed) {
        this.failed = failed;
    }

    public MatterSyncMessage(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.failed = false;
    }

    public static void encode(MatterSyncMessage packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static MatterSyncMessage decode(PacketBuffer buffer) {
        try {
            return new MatterSyncMessage(buffer.readBlockPos());
        }
        catch (Exception e) {
            return new MatterSyncMessage(false);
        }
    }

    public static void handle(MatterSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (!msg.failed) {
            context.enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                ServerPlayerEntity serverPlayerEntity = context.getSender();
                if (serverPlayerEntity != null && world != null && world.isBlockPresent(msg.blockPos) && serverPlayerEntity.world != null && serverPlayerEntity.world.isBlockPresent(msg.blockPos)) {
                    // client -> server type of message, everything is safe to be used
                    TileEntity serverTileEntity = serverPlayerEntity.world.getTileEntity(msg.blockPos);
                    TileEntity clientTileEntity = world.getTileEntity(msg.blockPos);
                    if (serverTileEntity instanceof MatterRecyclerTitle && clientTileEntity instanceof MatterRecyclerTitle) {
                        ((MatterRecyclerTitle) clientTileEntity).updateClient(
                                ((MatterRecyclerTitle) serverTileEntity).getCurrentMatter()
                        );
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}