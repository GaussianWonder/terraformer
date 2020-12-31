package com.gaussianwonder.terraformer.networking;

import com.gaussianwonder.terraformer.TerraformerMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel MATTER_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("terraformer", "matter-recycle"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        MATTER_CHANNEL.messageBuilder(MatterUpdateMessage.class, nextID())
                .encoder(MatterUpdateMessage::encode)
                .decoder(MatterUpdateMessage::decode)
                .consumer(MatterUpdateMessage::handle)
                .add();
    }

//    public static void updateMatterOn(MatterUpdateMessage packet, ServerPlayerEntity player) {
//        MATTER_CHANNEL.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
//    }
//
//    public static void requestMatterUpdate(MatterUpdateMessage packet) {
//        MATTER_CHANNEL.sendToServer(packet);
//    }

    public static int nextID() {
        return ID++;
    }
}
