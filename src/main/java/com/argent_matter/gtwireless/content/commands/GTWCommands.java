package com.argent_matter.gtwireless.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.List;

public class GTWCommands {
    private static List<String> aliases = List.of("gtw", "gtwireless");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        List<LiteralCommandNode<CommandSourceStack>> rootCommands = aliases.stream().map(
                alias -> Commands.literal(alias).build()
        ).toList();

        List<LiteralCommandNode<CommandSourceStack>> subCommands = List.of(
                SetTeamCommand.build(),
                StatusCommand.build(),
                SetDirtyCommand.build()
        );

        rootCommands.forEach( root -> {
            subCommands.forEach(root::addChild);
            dispatcher.getRoot().addChild(root);
        });
    }
}