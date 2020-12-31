package com.gaussianwonder.terraformer.networking;

import com.gaussianwonder.terraformer.setup.blocks.tiles.MatterRecyclerTitle;
import com.gaussianwonder.terraformer.setup.capabilities.storage.MatterStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MatterUpdateMessage {
    protected float matter;
    protected float capacity;
    protected float maxReceive;
    protected float maxExtract;

    protected BlockPos blockPos;

    protected boolean failed;

    private MatterUpdateMessage(boolean failed) {
        this.failed = failed;
    }

    public MatterUpdateMessage(MatterStorage matterStorage, BlockPos blockPos) {
        this.matter = matterStorage.getMatterStored();
        this.capacity = matterStorage.getMaxMatterStored();
        this.maxReceive = matterStorage.getMaxReceived();
        this.maxExtract = matterStorage.getMaxExtract();

        this.blockPos = blockPos;

        this.failed = false;
    }

    public MatterUpdateMessage(float matter, float capacity, float maxReceive, float maxExtract, BlockPos blockPos) {
        this.matter = matter;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;

        this.blockPos = blockPos;

        this.failed = false;
    }

    public static void encode(MatterUpdateMessage packet, PacketBuffer buffer) {
        buffer.writeFloat(packet.matter);
        buffer.writeFloat(packet.capacity);
        buffer.writeFloat(packet.maxReceive);
        buffer.writeFloat(packet.maxExtract);

        buffer.writeBlockPos(packet.blockPos);
    }

    public static MatterUpdateMessage decode(PacketBuffer buffer) {
        try {
            return new MatterUpdateMessage(
                    buffer.readFloat(),     // matter
                    buffer.readFloat(),     // capacity
                    buffer.readFloat(),     // maxReceive
                    buffer.readFloat(),     // maxExtract
                    buffer.readBlockPos()   // blockPos
            );
        }
        catch (IndexOutOfBoundsException e) {
            return new MatterUpdateMessage(true);
        }
    }

//    public static boolean handle(MatterUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            // Work that needs to be threadsafe (most work)
//            ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
//            int a = sender.currentWindowId;
//            // do stuff
//        });
//
//        ctx.get().setPacketHandled(true);
//        return true;
//    }

    public static void handle(MatterUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, ctx.get())); //TODO make it safe
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(MatterUpdateMessage msg, NetworkEvent.Context ctx) {
        if (!msg.failed) {
            World world = Minecraft.getInstance().world;
            if (world != null) {
                TileEntity tileEntity = world.getTileEntity(msg.blockPos);
                if (tileEntity instanceof MatterRecyclerTitle) {
                    ((MatterRecyclerTitle) tileEntity).clientUpdateMatter(msg.matter, msg.capacity, msg.maxReceive, msg.maxExtract);
                }
            }
        }

        ctx.setPacketHandled(true);
    }
}
