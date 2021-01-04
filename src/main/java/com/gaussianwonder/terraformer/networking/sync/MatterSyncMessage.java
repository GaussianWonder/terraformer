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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MatterSyncMessage {

    public static class Request {
        private BlockPos blockPos;
        private boolean failed;

        public Request(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.failed = false;
        }

        private Request(boolean failed) {
            this.failed = failed;
        }

        public static void encode(Request packet, PacketBuffer buffer) {
            buffer.writeBlockPos(packet.blockPos);
        }

        public static Request decode(PacketBuffer buffer) {
            try {
                return new Request(buffer.readBlockPos());
            }
            catch (Exception e) {
                return new Request(false);
            }
        }

        public static void handle(Request msg, Supplier<NetworkEvent.Context> ctx) {
            if(!msg.failed) {
                ctx.get().enqueueWork(() -> {
                    World world = Minecraft.getInstance().world;
                    if (world != null && world.isBlockPresent(msg.blockPos)) {
                        System.out.println("Blockpos is valid");
                        ServerPlayerEntity serverPlayerEntity = ctx.get().getSender();

                        if(serverPlayerEntity != null) {
                            TileEntity tileEntity = serverPlayerEntity.world.getTileEntity(msg.blockPos);
                            if (tileEntity instanceof MatterRecyclerTitle) {
                                MatterStorage updatedMatterStorage = ((MatterRecyclerTitle) tileEntity).getCurrentMatter();

                                System.out.println("Send the updated thing " + updatedMatterStorage.getMatterStored() + " on " + (serverPlayerEntity.world.isRemote ? "Client" : "Server"));
                                PacketHandler.SYNC_CHANNEL.sendTo(
                                        new Response(
                                                updatedMatterStorage,
                                                msg.blockPos
                                        ),
                                        serverPlayerEntity.connection.netManager,
                                        NetworkDirection.PLAY_TO_CLIENT
                                );
                            }
                        }
                    }
                });
            }
            ctx.get().setPacketHandled(true);
        }
    }

    public static class Response {
        private MatterStorage matterStorage;
        private BlockPos blockPos;

        private boolean failed;

        private Response(boolean failed) {
            this.failed = failed;
        }

        public Response(MatterStorage matterStorage, BlockPos blockPos) {
            this.matterStorage = matterStorage;
            this.blockPos = blockPos;
            this.failed = false;
        }

        public static void encode(Response packet, PacketBuffer buffer) {
            buffer.writeFloat(packet.matterStorage.getMatterStored());
            buffer.writeFloat(packet.matterStorage.getMaxMatterStored());
            buffer.writeFloat(packet.matterStorage.getMaxReceived());
            buffer.writeFloat(packet.matterStorage.getMaxExtract());

            buffer.writeBlockPos(packet.blockPos);
        }

        public static Response decode(PacketBuffer buffer) {
            try {
                return new Response(
                        new MatterStorage(
                                buffer.readFloat(),
                                buffer.readFloat(),
                                buffer.readFloat(),
                                buffer.readFloat()
                        ),
                        buffer.readBlockPos()
                );
            }
            catch (Exception e) {
                return new Response(false);
            }
        }

        public static void handle(Response msg, Supplier<NetworkEvent.Context> ctx) {
            if(!msg.failed) {
                ctx.get().enqueueWork(() -> {
                    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> handleClient(msg));
                });
            }
            ctx.get().setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void handleClient(Response msg) {
            World world = Minecraft.getInstance().world;
            if (world != null && world.isBlockPresent(msg.blockPos)) {
                TileEntity tileEntity = world.getTileEntity(msg.blockPos);
                System.out.println("Received the updated thing");
                if (tileEntity instanceof MatterRecyclerTitle)
                    ((MatterRecyclerTitle) tileEntity).updateClient(msg.matterStorage);
            }
        }
    }

}
