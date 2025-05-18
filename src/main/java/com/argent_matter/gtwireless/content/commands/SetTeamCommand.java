package com.argent_matter.gtwireless.content.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.argent_matter.gtwireless.data.GTWSavedData;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.UUID;

public class SetTeamCommand {

    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("setTeam")
                .requires(source -> source.hasPermission(2)) // Needs OP
                .then(Commands.argument("Player1", StringArgumentType.string())
                        .then(Commands.argument("Player2", StringArgumentType.string())
                                .executes(context -> {
                                    String player1Name = StringArgumentType.getString(context, "Player1");
                                    String player2Name = StringArgumentType.getString(context, "Player2");

                                    ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayerByName(player1Name);
                                    ServerPlayer player2 = context.getSource().getServer().getPlayerList().getPlayerByName(player2Name);

                                    if (player1 == null || player2 == null) {
                                        context.getSource().sendSystemMessage(Component.literal("At least one of the Players specified cannot be not found."));
                                        return 0;
                                    }

                                    UUID player1UUID = player1.getUUID();
                                    UUID player2UUID = player2.getUUID();

                                    GTWSavedData savedData = GTWSavedData.get(context.getSource().getLevel());
                                    savedData.getWirelessHolder().getPlayerTeams().put(player1UUID, player2UUID);
                                    savedData.setDirty();

                                    context.getSource().sendSystemMessage(Component.literal(player1.getName().getString() + " successfully joined " + player2.getName().getString() + "'s team."));
                                    return 1;
                                })))
                .build();
    }
}
