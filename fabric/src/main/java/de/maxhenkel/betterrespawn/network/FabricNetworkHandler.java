package de.maxhenkel.betterrespawn.network;

import de.maxhenkel.betterrespawn.BetterRespawnMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHandler implements NetworkHandler{

    public static void init() {
        PayloadTypeRegistry.playC2S().register(RespawnAtRespawnPointPayload.TYPE, RespawnAtRespawnPointPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RespawnAtRespawnPointPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            BetterRespawnMod.RESPAWN_MANAGER.respawnAtRespawnPoint(player);
        });
    }

    @Override
    public void sendRespawnAtRespawnPointPacket() {
         ClientPlayNetworking.send(new RespawnAtRespawnPointPayload());
    }
    
    public record RespawnAtRespawnPointPayload() implements CustomPacketPayload {
        public static final Type<RespawnAtRespawnPointPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("better_respawn", "respawn_at_respawn_point"));                                             
        public static final StreamCodec<FriendlyByteBuf, RespawnAtRespawnPointPayload> STREAM_CODEC = StreamCodec.unit(new RespawnAtRespawnPointPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }
}
