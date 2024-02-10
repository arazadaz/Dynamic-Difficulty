package com.arazadaz.dd.commands;

import com.arazadaz.dd.api.DifficultyCalculator;
import com.arazadaz.dd.api.Modes;
import com.arazadaz.dd.api.origins.Origin;
import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.utilities.DDGeneralUtility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Optional;

public class DDCommands {

    private static int getNearestOutpostInfo(CommandContext<CommandSourceStack> context){

        CommandSourceStack src = context.getSource();
        String world = src.getLevel().dimension().location().getPath();

        Origin origin = OriginManager.getNearestOrigin(world, "default", src.getPosition());

        double calculatedDifficulty = DifficultyCalculator.getOriginDifficultyHere(origin, src.getPosition(), Modes.DifficultyType.SURFACE, Modes.RadiusMode.SQUARE, Optional.empty());
        DecimalFormat df = new DecimalFormat("###.##");

        String chatMessage = "Origin Info\n"
                + "\n"
                + "Located in dimension/s: " + origin.world + "\n"
                + "Position: " + origin.pos.toString() + "\n"
                + "Tags: " + Arrays.toString(origin.tags) + "\n"
                + "Calculated difficulty here: " + df.format(calculatedDifficulty) + "\n";

        DDGeneralUtility.sendChatMsg(chatMessage, src);

        return 1;
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("getNearestOutpostInfo").requires(commandSource ->
            commandSource.hasPermission(3))
        .executes(DDCommands::getNearestOutpostInfo));
    }
}
