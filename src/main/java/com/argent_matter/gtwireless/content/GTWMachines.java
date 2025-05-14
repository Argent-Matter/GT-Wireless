package com.argent_matter.gtwireless.content;

import com.argent_matter.gtwireless.GTWireless;
import com.argent_matter.gtwireless.content.hatches.CloudComputationHatchMachine;
import com.argent_matter.gtwireless.content.hatches.CloudDataHatchMachine;
import com.argent_matter.gtwireless.content.hatches.WirelessEnergyHatchPartMachine;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GTWMachines {
    public static final MachineDefinition[] WIRELESS_ENERGY_HATCH;
    public static final MachineDefinition[] WIRELESS_ENERGY_HATCH_4A;
    public static final MachineDefinition[] WIRELESS_ENERGY_HATCH_16A;

    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH;
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_4A;
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_16A;

    public static final MachineDefinition CLOUD_SERVER_HATCH;
    public static final MachineDefinition CLOUD_CLIENT_HATCH;

    public static final MachineDefinition CLOUD_DATA_RECEIVER_HATCH;
    public static final MachineDefinition CLOUD_DATA_TRANSMITTER_HATCH;

    private static @NotNull MachineBuilder<MachineDefinition> registerCloudComputationHatch(String name, String displayName, int tier, Function<
                IMachineBlockEntity, MetaMachine > constructor, String model, Component tooltip, PartAbility... abilities) {
        return GTWireless.REGISTRATE.machine(name, constructor).langValue(displayName).tier(tier).rotationState(RotationState.ALL).abilities(abilities).overlayTieredHullRenderer(model).tooltips(tooltip);
    }

    private static @NotNull MachineBuilder<MachineDefinition> registerCloudDataHatch(String name, String displayName, int tier, Function<
            IMachineBlockEntity, MetaMachine > constructor, String model, Component tooltip, PartAbility... abilities) {
        return GTWireless.REGISTRATE.machine(name, constructor).langValue(displayName).tier(tier).rotationState(RotationState.ALL).abilities(abilities).overlayTieredHullRenderer(model).tooltips(tooltip);
    }

    public static MachineDefinition[] registerTieredHatches(String name, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];

        for(int tier : tiers) {
            MachineBuilder<MachineDefinition> register = GTWireless.REGISTRATE.machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, (holder) -> (MetaMachine)factory.apply(holder, tier)).tier(tier);
            definitions[tier] = (MachineDefinition) builder.apply(tier, register);
        }

        return definitions;
    }

    public GTWMachines() {
        // Empty... was there something here?
    }

    public static void init() {

    }

    static {
        WIRELESS_ENERGY_HATCH = registerTieredHatches("wireless_energy_input_hatch", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.IN, 2, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.input.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);

        WIRELESS_ENERGY_HATCH_4A = registerTieredHatches("wireless_energy_input_hatch_4a", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.IN, 4, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " 4A Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.input.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);

        WIRELESS_ENERGY_HATCH_16A = registerTieredHatches("wireless_energy_input_hatch_16a", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.IN, 16, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " 16A Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.input.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);


        WIRELESS_OUTPUT_HATCH = registerTieredHatches("wireless_energy_output_hatch", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.OUT, 2, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.output.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);

        WIRELESS_OUTPUT_HATCH_4A = registerTieredHatches("wireless_energy_output_hatch_4a", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.OUT, 4, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " 4A Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.output.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);

        WIRELESS_OUTPUT_HATCH_16A = registerTieredHatches("wireless_energy_output_hatch_16a", (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, IO.OUT, 16, new Object[0]), (tier, builder) -> {
            String[] voltage = GTValues.VNF;
            return builder.langValue(voltage[tier] + " 16A Wireless Energy Hatch").rotationState(RotationState.ALL).abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY}).tooltips(new Component[]{Component.translatable("gtceu.universal.tooltip.voltage_in", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]}), Component.translatable("gtceu.universal.tooltip.amperage_in", new Object[]{2}), Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))}), Component.translatable("gtwireless.machine.wireless_energy_hatch.output.tooltip")}).overlayTieredHullRenderer("energy_hatch.input").register();
        }, GTMachineUtils.ALL_TIERS);

        CLOUD_SERVER_HATCH = registerCloudComputationHatch("cloud_server_hatch",  ChatFormatting.AQUA + "Cloud Server Hatch", 8, (holder) -> new CloudComputationHatchMachine(holder, true), "cloud_server_hatch", Component.translatable("gtwireless.machine.cloud_server_hatch.tooltip"), PartAbility.COMPUTATION_DATA_TRANSMISSION).register();
        CLOUD_CLIENT_HATCH = registerCloudComputationHatch("cloud_client_hatch", ChatFormatting.AQUA + "Cloud Client Hatch", 8, (holder) -> new CloudComputationHatchMachine(holder, false), "cloud_client_hatch", Component.translatable("gtwireless.machine.cloud_client_hatch.tooltip"), PartAbility.COMPUTATION_DATA_RECEPTION).register();

        CLOUD_DATA_TRANSMITTER_HATCH = registerCloudDataHatch("cloud_data_transmitter_hatch", ChatFormatting.AQUA + "Cloud Data Transmitter Hatch", 8, (holder) -> new CloudDataHatchMachine(holder, 8, IO.OUT), "cloud_data_transmitter_hatch", Component.translatable("gtwireless.machine.cloud_data_transmitter_hatch.tooltip"), PartAbility.DATA_ACCESS).register();
        CLOUD_DATA_RECEIVER_HATCH = registerCloudDataHatch("cloud_data_receiver_hatch", ChatFormatting.AQUA + "Cloud Data Receiver Hatch", 8, (holder) -> new CloudDataHatchMachine(holder, 8, IO.IN), "cloud_data_receiver_hatch", Component.translatable("gtwireless.machine.cloud_data_receiver_hatch.tooltip"), PartAbility.DATA_ACCESS).register();
    }
}
