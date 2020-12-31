package com.gaussianwonder.terraformer.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MatterPacket {
    private float matter;

    public MatterPacket(float matter_) {
        this.matter = matter_;
    }

    public static void encode(MatterPacket packet, PacketBuffer buffer) {
        buffer.writeFloat(packet.matter);
    }

    public static MatterPacket decode(PacketBuffer buffer) {
        return new MatterPacket(buffer.readFloat());
    }

    public static boolean handle(MatterPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be threadsafe (most work)
            ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
            // do stuff
        });

        ctx.get().setPacketHandled(true);
        return true;
    }
}

