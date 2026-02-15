package de.maxhenkel.betterrespawn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    
    @Shadow private int delayTicker;
    private Button respawnAtSpawnButton;

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addRespawnAtSpawnButton(CallbackInfo ci) {
        respawnAtSpawnButton = this.addRenderableWidget(
            Button.builder(Component.literal("Respawn Nearby"), button -> {
                BetterRespawnMod.NETWORK_HANDLER.sendRespawnNearbyPacket();
                Minecraft.getInstance().getConnection().send(
                    new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN)
                );
            })
            .bounds(this.width / 2 - 100, this.height / 4 + 48, 200, 20)
            .build()
        );
        respawnAtSpawnButton.active = false;
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (respawnAtSpawnButton != null) {
            respawnAtSpawnButton.active = delayTicker >= 20;
        }
    }
}
