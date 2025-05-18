package com.argent_matter.gtwireless.content;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import com.argent_matter.gtwireless.GTWireless;
import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;

public class GTWRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        RegisterWirelessEnergyRecipes(provider);
        RegisterCloudRecipes(provider);
        RegisterMiscRecipes(provider);
    }

    public static void RegisterWirelessEnergyRecipes(Consumer<FinishedRecipe> provider) {
        ImmutableMap<Integer, BlockEntry<Block>> TIER_TO_CASING;
        ImmutableMap.Builder<Integer, BlockEntry<Block>> builder = ImmutableMap.builder();
        builder
                .put(ULV, GTBlocks.MACHINE_CASING_ULV)
                .put(LV, GTBlocks.MACHINE_CASING_LV)
                .put(MV, GTBlocks.MACHINE_CASING_MV)
                .put(HV, GTBlocks.MACHINE_CASING_HV)
                .put(EV, GTBlocks.MACHINE_CASING_EV)
                .put(IV, GTBlocks.MACHINE_CASING_IV)
                .put(LuV, GTBlocks.MACHINE_CASING_LuV)
                .put(ZPM, GTBlocks.MACHINE_CASING_ZPM)
                .put(UV, GTBlocks.MACHINE_CASING_UV)
                .put(UHV, GTBlocks.MACHINE_CASING_UHV)
                .put(UEV, GTBlocks.MACHINE_CASING_UEV)
                .put(UIV, GTBlocks.MACHINE_CASING_UIV)
                .put(UXV, GTBlocks.MACHINE_CASING_UXV)
                .put(OpV, GTBlocks.MACHINE_CASING_OpV)
                .put(MAX, GTBlocks.MACHINE_CASING_MAX);
        TIER_TO_CASING = builder.build();

        ImmutableMap<Integer, Material> TIER_TO_SUPERCONDUCTOR;
        ImmutableMap.Builder<Integer, Material> builder2 = ImmutableMap.builder();
        builder2
                .put(ULV, GTMaterials.RedAlloy)
                .put(LV, GTMaterials.ManganesePhosphide)
                .put(MV, GTMaterials.MagnesiumDiboride)
                .put(HV, GTMaterials.MercuryBariumCalciumCuprate)
                .put(EV, GTMaterials.UraniumTriplatinum)
                .put(IV, GTMaterials.SamariumIronArsenicOxide)
                .put(LuV, GTMaterials.IndiumTinBariumTitaniumCuprate)
                .put(ZPM, GTMaterials.UraniumRhodiumDinaquadide)
                .put(UV, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide)
                .put(UHV, GTMaterials.RutheniumTriniumAmericiumNeutronate);
        TIER_TO_SUPERCONDUCTOR = builder2.build();

        ImmutableMap<Integer, TagKey<?>> TIER_TO_CIRCUIT;
        ImmutableMap.Builder<Integer, TagKey<?>> builder3 = ImmutableMap.builder();
        builder3
                .put(ULV, CustomTags.ULV_CIRCUITS)
                .put(LV, CustomTags.LV_CIRCUITS)
                .put(MV, CustomTags.MV_CIRCUITS)
                .put(HV, CustomTags.HV_CIRCUITS)
                .put(EV, CustomTags.EV_CIRCUITS)
                .put(IV, CustomTags.IV_CIRCUITS)
                .put(LuV, CustomTags.LuV_CIRCUITS)
                .put(ZPM, CustomTags.ZPM_CIRCUITS)
                .put(UV, CustomTags.UV_CIRCUITS)
                .put(UHV, CustomTags.UHV_CIRCUITS);
        TIER_TO_CIRCUIT = builder3.build();

        ImmutableMap<Integer, ItemEntry<?>> TIER_TO_VOLTAGE_COIL;
        ImmutableMap.Builder<Integer, ItemEntry<?>> builder4 = ImmutableMap.builder();
        builder4
                .put(ULV, GTItems.VOLTAGE_COIL_ULV)
                .put(LV, GTItems.VOLTAGE_COIL_LV)
                .put(MV, GTItems.VOLTAGE_COIL_MV)
                .put(HV, GTItems.VOLTAGE_COIL_HV)
                .put(EV, GTItems.VOLTAGE_COIL_EV)
                .put(IV, GTItems.VOLTAGE_COIL_IV)
                .put(LuV, GTItems.VOLTAGE_COIL_LuV)
                .put(ZPM, GTItems.VOLTAGE_COIL_ZPM)
                .put(UV, GTItems.VOLTAGE_COIL_UV)
                .put(UHV, GTItems.FIELD_GENERATOR_UV);
        TIER_TO_VOLTAGE_COIL = builder4.build();

        Integer TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_ENERGY_HATCH) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_EXPULSOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 2)
                    .inputItems(TagPrefix.wireGtDouble, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 4)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 1 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 1).EUt(V[TIER]).save(provider);
        }

        TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_ENERGY_HATCH_4A) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, "4a_" + machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_EXPULSOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 4)
                    .inputItems(TagPrefix.wireGtQuadruple, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 8)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 2 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 2).EUt(V[TIER]).save(provider);

        }

        TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_ENERGY_HATCH_16A) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, "16a_" + machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_EXPULSOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 2 * 8)
                    .inputItems(TagPrefix.wireGtHex, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 8)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 8 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 8).EUt(V[TIER]).save(provider);
        }

        TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_OUTPUT_HATCH) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_INFLUXOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 2)
                    .inputItems(TagPrefix.wireGtDouble, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 4)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 1 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 1 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 1).EUt(V[TIER]).save(provider);
        }

        TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_OUTPUT_HATCH_4A) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, "4a_" + machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_INFLUXOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 4)
                    .inputItems(TagPrefix.wireGtQuadruple, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 8)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 2 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 2 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 2).EUt(V[TIER]).save(provider);
        }

        TIER = -1;
        for (MachineDefinition machine : GTWMachines.WIRELESS_OUTPUT_HATCH_16A) {
            TIER++;
            if (TIER > UHV) break;
            System.out.println("TIER: " + TIER);
            GTRecipeTypes.ASSEMBLY_LINE_RECIPES.recipeBuilder(GTWireless.MOD_ID, "16a_" + machine.get().self().getDescriptionId())
                    .inputItems(TIER_TO_CASING.get(TIER))
                    .inputItems(GTWItems.ETHER_INFLUXOR)
                    .inputItems(TIER_TO_CIRCUIT.get(TIER), 2 * 8)
                    .inputItems(TagPrefix.wireGtHex, TIER_TO_SUPERCONDUCTOR.get(TIER), 8)
                    .inputItems(TIER_TO_VOLTAGE_COIL.get(TIER), 8)
                    .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 32)
                    .inputFluids(GTMaterials.SodiumPotassium.getFluid((int) (1000 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.Lubricant.getFluid((int) (2000 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid((int) (288 * 8 * (1 + TIER))))
                    .inputFluids(GTMaterials.Naquadria.getFluid((int) (144 * 8 * (1 + TIER))))
                    .outputItems(machine)
                    .duration(600 * 8).EUt(V[TIER]).save(provider);
        }
    }

    public static void RegisterCloudRecipes(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWMachines.CLOUD_DATA_RECEIVER_HATCH.get().self().getDescriptionId())
                .inputItems(GTResearchMachines.DATA_HATCH_RECEIVER)
                .inputItems(GTItems.SENSOR_ZPM)
                .inputItems(GTItems.FIELD_GENERATOR_ZPM)
                .inputItems(CustomTags.ZPM_CIRCUITS, 2)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(144 * 8))
                .outputItems(GTWMachines.CLOUD_DATA_RECEIVER_HATCH)
                .addCondition(GTRecipeConditions.CLEANROOM.factory.createDefault())
                .duration(400).EUt(V[ZPM]).save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWMachines.CLOUD_DATA_TRANSMITTER_HATCH.get().self().getDescriptionId())
                .inputItems(GTResearchMachines.DATA_HATCH_TRANSMITTER)
                .inputItems(GTItems.EMITTER_ZPM)
                .inputItems(GTItems.FIELD_GENERATOR_ZPM)
                .inputItems(CustomTags.ZPM_CIRCUITS, 2)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(144 * 8))
                .outputItems(GTWMachines.CLOUD_DATA_TRANSMITTER_HATCH)
                .addCondition(GTRecipeConditions.CLEANROOM.factory.createDefault())
                .duration(400).EUt(V[ZPM]).save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWMachines.CLOUD_CLIENT_HATCH.get().self().getDescriptionId())
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_RECEIVER)
                .inputItems(GTItems.SENSOR_UV, 2)
                .inputItems(GTItems.FIELD_GENERATOR_UV)
                .inputItems(GTItems.TOOL_DATA_MODULE)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(144 * 16))
                .outputItems(GTWMachines.CLOUD_CLIENT_HATCH)
                .addCondition(GTRecipeConditions.CLEANROOM.factory.createDefault())
                .duration(600).EUt(V[UV]).save(provider);

        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWMachines.CLOUD_SERVER_HATCH.get().self().getDescriptionId())
                .inputItems(GTResearchMachines.COMPUTATION_HATCH_TRANSMITTER)
                .inputItems(GTItems.EMITTER_UV, 2)
                .inputItems(GTItems.FIELD_GENERATOR_UV)
                .inputItems(GTItems.TOOL_DATA_MODULE)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputFluids(GTMaterials.Polybenzimidazole.getFluid(144 * 16))
                .outputItems(GTWMachines.CLOUD_SERVER_HATCH)
                .addCondition(GTRecipeConditions.CLEANROOM.factory.createDefault())
                .duration(600).EUt(V[UV]).save(provider);
    }

    public static void RegisterMiscRecipes(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.COMPRESSOR_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWItems.HYPERDENSE_NAQUADRIA_SOLID.get().asItem().getDescriptionId())
                .inputItems(TagPrefix.dust, GTMaterials.Naquadria, 32)
                .outputItems(GTWItems.HYPERDENSE_NAQUADRIA_SOLID, 1)
                .duration(600).EUt(V[ZPM]).save(provider);

        GTRecipeTypes.IMPLOSION_RECIPES.recipeBuilder(GTWireless.MOD_ID, GTWItems.ETHER_EXPULSOR.get().asItem().getDescriptionId())
                .inputItems(GTBlocks.INDUSTRIAL_TNT, 8)
                .inputItems(GTWItems.HYPERDENSE_NAQUADRIA_SOLID, 1)
                .outputItems(GTWItems.ETHER_INFLUXOR, 1)
                .outputItems(GTWItems.ETHER_EXPULSOR, 1)
                .duration(20).EUt(30).save(provider);
    }
}
