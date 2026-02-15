package de.maxhenkel.betterrespawn.network;

import de.maxhenkel.betterrespawn.BetterRespawnMod;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ForgeNetworkHandler implements NetworkHandler{

    public static class RespawnNearbyPacket {}

     private static final SimpleChannel CHANNEL = ChannelBuilder
          .named(ResourceLocation.fromNamespaceAndPath("better_respawn", "respawn_nearby"))
          .optional()
          .simpleChannel();

    public static void init() {
        CHANNEL.messageBuilder(RespawnNearbyPacket.class)
            .encoder((msg, buf) -> {})
            .decoder(buf -> new RespawnNearbyPacket())
            .consumerMainThread((msg, ctx) -> {
                ServerPlayer player = ctx.getSender();
                BetterRespawnMod.RESPAWN_MANAGER.respawnNearby(player);
            })
            .add();
        CHANNEL.build();
    }

    @Override
    public void sendRespawnNearbyPacket() {
        CHANNEL.send(new RespawnNearbyPacket(), PacketDistributor.SERVER.noArg());
    }
}