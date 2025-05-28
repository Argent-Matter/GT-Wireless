package com.argent_matter.gtwireless.datagen;

import com.argent_matter.gtwireless.GTWireless;
import com.tterrag.registrate.providers.ProviderType;

public class RegistrateDataGen {
    public static void init() {
        GTWireless.REGISTRATE.addDataGenerator(ProviderType.BLOCKSTATE, ModelProvider::init);
        GTWireless.REGISTRATE.addDataGenerator(ProviderType.LANG, EnUsProvider::init);
    }
}
