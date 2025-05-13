package com.argent_matter.gtwireless.datagen;

import com.argent_matter.gtwireless.GTWireless;
import com.gregtechceu.gtceu.api.GTValues;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

public class EnUsProvider extends LanguageProvider {
    public EnUsProvider(PackOutput output, String locale) {
        super(output, GTWireless.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("gtwireless.machine.wireless_energy_hatch.input.tooltip", "Wireless Energy Input for Multiblocks");
        add("gtwireless.machine.wireless_energy_hatch.output.tooltip", "Wireless Energy Output for Multiblocks");

        add("gtwireless.machine.cloud_computation_hatch.input.tooltip", "Wireless Computation Input for Multiblocks");
        add("gtwireless.machine.cloud_computation_hatch.output.tooltip", "Wireless Computation Output for Multiblocks");

        add("gtwireless.commands.wireless_team.success", "%s successfully joined %s's team!");
        add("gtwireless.commands.wireless_team.not_found", "At least one of the Players specified cannot be found!");
        add("gtwireless.commands.wireless_team.already_in_a_team", "%s must not be already in a team!");
        add("gtwireless.commands.status.not_player", "Status command sent by a non-player entity.");
        add("gtwireless.commands.setdirty", "Successfully set GT Wireless SavedData dirty!");

        addDirectBlock("cloud_client_hatch", "Cloud Client Computation Hatch");
        addDirectBlock("cloud_server_hatch", "Cloud Server Computation Hatch");

        for (int i = 0; i < GTValues.VN.length; i++) {
            String VN = GTValues.VN[i];
            String VNF = GTValues.VNF[i];

            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_input_hatch", VNF + " Wireless Energy Hatch");
            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_output_hatch", VNF + " Wireless Dynamo Hatch");

            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_input_hatch_4a", VNF + " 4A Wireless Energy Hatch");
            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_output_hatch_4a", VNF + " 4A Wireless Dynamo Hatch");

            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_input_hatch_16a", VNF + " 16A Wireless Energy Hatch");
            addDirectBlock(VN.toLowerCase(Locale.ROOT) + "_wireless_energy_output_hatch_16a", VNF + " 16A Wireless Dynamo Hatch");
        }
    }

    private void addDirectBlock(String key, String value) {
        add("block.gtwireless." + key, value);
    }
}
