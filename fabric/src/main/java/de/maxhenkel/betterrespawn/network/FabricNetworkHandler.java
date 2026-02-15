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
        PayloadTypeRegistry.playC2S().register(RespawnNearbyPayload.TYPE, RespawnNearbyPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RespawnNearbyPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            BetterRespawnMod.RESPAWN_MANAGER.respawnNearby(player);
        });
    }

    @Override
    public void sendRespawnNearbyPacket() {
         ClientPlayNetworking.send(new RespawnNearbyPayload());
    }
    
    public record RespawnNearbyPayload() implements CustomPacketPayload {
        public static final Type<RespawnNearbyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("better_respawn", "respawn_nearby"));                                             
        public static final StreamCodec<FriendlyByteBuf, RespawnNearbyPayload> STREAM_CODEC = StreamCodec.unit(new RespawnNearbyPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }
}
