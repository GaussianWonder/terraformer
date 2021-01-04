package com.gaussianwonder.terraformer.networking;

import com.gaussianwonder.terraformer.TerraformerMod;
import com.gaussianwonder.terraformer.networking.sync.MatterSyncMessage;
import net.minecraft.util.ResourceLocation;
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
        SYNC_CHANNEL.messageBuilder(MatterSyncMessage.class, nextID())
                .encoder(MatterSyncMessage::encode)
                .decoder(MatterSyncMessage::decode)
                .consumer(MatterSyncMessage::handle)
                .add();
    }

    private static int nextID() {
        return ID++;
    }
}
