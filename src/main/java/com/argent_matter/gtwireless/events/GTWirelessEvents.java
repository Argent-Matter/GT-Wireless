package com.argent_matter.gtwireless.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.argent_matter.gtwireless.content.commands.GTWCommands;
import com.argent_matter.gtwireless.data.GTWSavedData;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = "gtwireless")
public class GTWirelessEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerLevel level = (ServerLevel) event.getEntity().level();
        UUID playerUUID = event.getEntity().getUUID();
        GTWSavedData.WirelessHolder holder = GTWSavedData.get(level).getWirelessHolder();

        if (holder.getPlayerTeams().get(playerUUID) == null) {
            holder.putPlayer(playerUUID);
            GTWSavedData.get(level).setDirty();
            event.getEntity().sendSystemMessage(Component.literal("You have been added to the GT Wireless system. GLHF <3"));
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        GTWCommands.register(event.getDispatcher());
    }
}
