package com.argent_matter.gtwireless.content.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.argent_matter.gtwireless.data.GTWSavedData;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class SetDirtyCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setDirty")
                .requires(source -> source.hasPermission(2)) // Needs OP
                .executes(context -> {
                    GTWSavedData savedData = GTWSavedData.get(context.getSource().getLevel());
                    savedData.setDirty();

                    context.getSource().sendSystemMessage(Component.translatable("gtwireless.commands.setdirty"));
                    return 1;
                }).build();
    }
}
