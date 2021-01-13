package com.gaussianwonder.terraformer.networking.sync;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterFuserTile;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTile;
import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterStorageTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MachineSyncMessage {
    private BlockPos blockPos;
    private boolean failed;

    MachineSyncMessage(boolean failed) {
        this.failed = failed;
    }

    public MachineSyncMessage(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.failed = false;
    }

    public static void encode(MachineSyncMessage packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static MachineSyncMessage decode(PacketBuffer buffer) {
        try {
            return new MachineSyncMessage(buffer.readBlockPos());
        }
        catch (Exception e) {
            return new MachineSyncMessage(false);
        }
    }

    public static void handle(MachineSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (!msg.failed) {
            context.enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                ServerPlayerEntity serverPlayerEntity = context.getSender();
                if (serverPlayerEntity != null && world != null && world.isBlockPresent(msg.blockPos) && serverPlayerEntity.world != null && serverPlayerEntity.world.isBlockPresent(msg.blockPos)) {
                    // client -> server type of message, everything is safe to be used
                    TileEntity serverTileEntity = serverPlayerEntity.world.getTileEntity(msg.blockPos);
                    TileEntity clientTileEntity = world.getTileEntity(msg.blockPos);
                    if (serverTileEntity instanceof MatterRecyclerTile && clientTileEntity instanceof MatterRecyclerTile) {
                        ((MatterRecyclerTile) clientTileEntity).updateClient(
                                ((MatterRecyclerTile) serverTileEntity).getCurrentMachine()
                        );
                    }
                    if (serverTileEntity instanceof MatterStorageTile && clientTileEntity instanceof MatterStorageTile) {
                        ((MatterStorageTile) clientTileEntity).updateClient(
                                ((MatterStorageTile) serverTileEntity).getCurrentMachine()
                        );
                    }
                    if (serverTileEntity instanceof MatterFuserTile && clientTileEntity instanceof MatterFuserTile) {
                        ((MatterFuserTile) clientTileEntity).updateClient(
                                ((MatterFuserTile) serverTileEntity).getCurrentMachine()
                        );
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}
