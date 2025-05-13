package com.argent_matter.gtwireless.content.commands;

import com.argent_matter.gtwireless.data.GTWSavedData;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;

public class StatusCommand {
    public static LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("status")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    ServerLevel level = ctx.getSource().getLevel();

                    if (player == null) {
                        ctx.getSource().sendFailure(Component.translatable("gtwireless.commands.status.not_player"));
                        return 0;
                    }

                    player.sendSystemMessage(GTWSavedData.get(level).getWirelessHolder().statusOf(player.getUUID()));
                    return 1;
                })
                .build();
    }
}
