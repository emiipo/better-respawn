package de.maxhenkel.betterrespawn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.maxhenkel.betterrespawn.BetterRespawnMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;

@Mixin(DeathScreen.class)              
public abstract class DeathScreenMixin extends Screen {
    
    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addRespawnAtRespawnPointButton(CallbackInfo ci) {
        this.addRenderableWidget(
            Button.builder(Component.literal("Respawn at Respawn Point"), button -> {
                BetterRespawnMod.NETWORK_HANDLER.sendRespawnAtRespawnPointPacket();
                Minecraft.getInstance().getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            })
            .bounds(this.width / 2 - 100, this.height / 4 + 72 - 28, 200, 20)
            .build()
        );
    }
}
