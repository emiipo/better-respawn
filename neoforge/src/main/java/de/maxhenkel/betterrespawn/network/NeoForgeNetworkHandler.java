package de.maxhenkel.betterrespawn.network;

import de.maxhenkel.betterrespawn.BetterRespawnMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class NeoForgeNetworkHandler implements NetworkHandler{

    @Override
    public void sendRespawnAtRespawnPointPacket() {
        PacketDistributor.sendToServer(new RespawnAtRespawnPointPayload());
    }
    
    public record RespawnAtRespawnPointPayload() implements CustomPacketPayload {                                                                     
        public static final Type<RespawnAtRespawnPointPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("better_respawn", "respawn_at_respawn_point"));
        public static final StreamCodec<FriendlyByteBuf, RespawnAtRespawnPointPayload> STREAM_CODEC = StreamCodec.unit(new RespawnAtRespawnPointPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
            RespawnAtRespawnPointPayload.TYPE,
            RespawnAtRespawnPointPayload.STREAM_CODEC,
            (payload, context) -> {
                ServerPlayer player = (ServerPlayer) context.player();
                BetterRespawnMod.RESPAWN_MANAGER.respawnAtRespawnPoint(player);
            }
        );
    }
}
