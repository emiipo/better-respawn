package de.maxhenkel.betterrespawn.network;

import de.maxhenkel.betterrespawn.BetterRespawnMod;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ForgeNetworkHandler implements NetworkHandler{

    public static class RespawnAtRespawnPointPacket {}

     private static final SimpleChannel CHANNEL = ChannelBuilder
          .named(ResourceLocation.fromNamespaceAndPath("better_respawn", "respawn_at_respawn_point"))
          .optional()
          .simpleChannel();

    public static void init() {
        CHANNEL.messageBuilder(RespawnAtRespawnPointPacket.class)
            .encoder((msg, buf) -> {})
            .decoder(buf -> new RespawnAtRespawnPointPacket())
            .consumerMainThread((msg, ctx) -> {
                ServerPlayer player = ctx.getSender();
                BetterRespawnMod.RESPAWN_MANAGER.respawnAtRespawnPoint(player);
            })
            .add();
        CHANNEL.build();
    }

    @Override
    public void sendRespawnAtRespawnPointPacket() {
        CHANNEL.send(new RespawnAtRespawnPointPacket(), PacketDistributor.SERVER.noArg());
    }
}