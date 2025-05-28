package com.argent_matter.gtwireless.content;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import com.argent_matter.gtwireless.GTWireless;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.awt.*;

public class GTWItems {

    public static void init() {}

    public static final ItemEntry<ComponentItem> HYPERDENSE_NAQUADRIA_SOLID = GTWireless.REGISTRATE.item("hyperdense_naquadria_solid", ComponentItem::create)
            .lang(ChatFormatting.WHITE + "Hyper-Dense Naquadria Solid")
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtwireless.item.hyperdense_naquadria_solid.tooltip"));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ETHER_INFLUXOR = GTWireless.REGISTRATE.item("ether_influxor", ComponentItem::create)
            .lang(ChatFormatting.WHITE + "Ether Influxor")
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtwireless.item.ether_influxor.tooltip"));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ETHER_EXPULSOR = GTWireless.REGISTRATE.item("ether_expulsor", ComponentItem::create)
            .lang(ChatFormatting.WHITE + "Ether Expulsor")
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtwireless.item.ether_expulsor.tooltip"));
            })))
            .register();

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent components) {
        return item -> item.attachComponents(components);
    }
}
