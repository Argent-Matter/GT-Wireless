package com.argent_matter.gtwireless.content;


import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class GTWRecipes {
    public static void init(Consumer<FinishedRecipe> provider) {
        RegisterWirelessEnergyRecipes(provider);
        RegisterWirelessComputationRecipes(provider);
    }

    public static void RegisterWirelessEnergyRecipes(Consumer<FinishedRecipe> provider) {

    }

    public static void RegisterWirelessComputationRecipes(Consumer<FinishedRecipe> provider) {

    }
}
