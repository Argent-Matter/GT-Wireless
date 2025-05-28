package com.argent_matter.gtwireless.datagen;

import com.gregtechceu.gtceu.api.GTValues;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.data.LanguageProvider;

import com.argent_matter.gtwireless.GTWireless;

import java.util.Locale;

public class EnUsProvider {
    public static void init(RegistrateLangProvider provider) {
        provider.add("gtwireless.machine.wireless_energy_hatch.input.tooltip", "Wireless Energy Input for Multiblocks");
        provider.add("gtwireless.machine.wireless_energy_hatch.output.tooltip", "Wireless Energy Output for Multiblocks");

        provider.add("gtwireless.machine.cloud_client_hatch.tooltip", "Wireless Computation Input for Multiblocks");
        provider.add("gtwireless.machine.cloud_server_hatch.tooltip", "Wireless Computation Output for Multiblocks");

        provider.add("gtwireless.machine.cloud_data_receiver_hatch.tooltip", "Cloud Assembly Line Research Input Hatch (For the Assembly Line)");
        provider.add("gtwireless.machine.cloud_data_transmitter_hatch.tooltip", "Cloud Assembly Line Research Output Hatch (For the Data Bank)");

        provider.add("gtwireless.commands.wireless_team.success", "%s successfully joined %s's team!");
        provider.add("gtwireless.commands.wireless_team.not_found", "At least one of the Players specified cannot be found!");
        provider.add("gtwireless.commands.wireless_team.already_in_a_team", "%s must not be already in a team!");
        provider.add("gtwireless.commands.status.not_player", "Status command sent by a non-player entity.");
        provider.add("gtwireless.commands.setdirty", "Successfully set GT Wireless SavedData dirty!");

        provider.add("gtwireless.item.hyperdense_naquadria_solid.tooltip", Component.literal("It's... angry.").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY).getString());
        provider.add("gtwireless.item.ether_influxor.tooltip", Component.literal("OOOOoooooo......").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY).getString());
        provider.add("gtwireless.item.ether_expulsor.tooltip", Component.literal("......ooooooOOOO").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY).getString());
    }
}
