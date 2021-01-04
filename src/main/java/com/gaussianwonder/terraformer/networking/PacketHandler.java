package com.gaussianwonder.terraformer.networking;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.networking.sync.MatterSyncMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TerraformerMod.MOD_ID, "sync"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        SYNC_CHANNEL.messageBuilder(MatterSyncMessage.Request.class, nextID())
                .encoder(MatterSyncMessage.Request::encode)
                .decoder(MatterSyncMessage.Request::decode)
                .consumer(MatterSyncMessage.Request::handle)
                .add();

        SYNC_CHANNEL.messageBuilder(MatterSyncMessage.Response.class, nextID())
                .encoder(MatterSyncMessage.Response::encode)
                .decoder(MatterSyncMessage.Response::decode)
                .consumer(MatterSyncMessage.Response::handle)
                .add();
    }

    public static void syncClient(MatterSyncMessage.Response packet, ServerPlayerEntity player) {
        SYNC_CHANNEL.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void requestSync(MatterSyncMessage packet) {
        SYNC_CHANNEL.sendToServer(packet);
    }

    private static int nextID() {
        return ID++;
    }
}
