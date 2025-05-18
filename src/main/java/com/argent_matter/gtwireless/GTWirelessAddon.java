package com.argent_matter.gtwireless;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import com.argent_matter.gtwireless.content.GTWItems;
import com.argent_matter.gtwireless.content.GTWRecipes;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@GTAddon
public class GTWirelessAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return GTWireless.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        GTWItems.init();
    }

    @Override
    public String addonModId() {
        return GTWireless.MOD_ID;
    }

    @Override
    public void registerTagPrefixes() {
        // CustomTagPrefixes.init();
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        GTWRecipes.init(provider);
    }
}
