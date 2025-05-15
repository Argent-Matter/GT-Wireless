package com.argent_matter.gtwireless.content;

import com.argent_matter.gtwireless.GTWireless;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.awt.*;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;

public class GTWItems {
    public static final ItemEntry<Item> HYPERDENSE_NAQUADRIA_SOLID;

    public static final ItemEntry<Item> ETHER_INFLUXOR;
    public static final ItemEntry<Item> ETHER_EXPULSOR;

    public static void init() {

    }

    static {
        HYPERDENSE_NAQUADRIA_SOLID = GTWireless.REGISTRATE.item("hyperdense_naquadria_solid", Item::new).lang(ChatFormatting.WHITE + "Ether Influxor").register();

        ETHER_INFLUXOR = GTWireless.REGISTRATE.item("ether_influxor", Item::new).lang(ChatFormatting.WHITE + "Ether Influxor").register();
        ETHER_EXPULSOR = GTWireless.REGISTRATE.item("ether_expulsor", Item::new).lang(ChatFormatting.WHITE + "Ether Expulsor").register();
    }
}
