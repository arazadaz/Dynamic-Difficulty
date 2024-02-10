package com.arazadaz.dd.commands;

import com.arazadaz.dd.api.DifficultyCalculator;
import com.arazadaz.dd.api.Modes;
import com.arazadaz.dd.api.origins.Origin;
import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.config.Config;
import com.arazadaz.dd.utilities.DDGeneralUtility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Optional;

public class DDCommands {

    private static int getNearestOriginInfo(CommandContext<CommandSourceStack> context){

        CommandSourceStack src = context.getSource();
        String world = src.getLevel().dimension().location().getPath();
        Origin origin;

        int numArguments = context.getInput().split(" ").length -1;
        if(numArguments==1){
            String type = context.getArgument("type", String.class);
            origin = OriginManager.getNearestOrigin(world, type, src.getPosition());
        }else{
            origin = OriginManager.getNearestOrigin(world, "default", src.getPosition());
        }



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

    private static int registerNewOrigin(CommandContext<CommandSourceStack> context){

        CommandSourceStack src = context.getSource();
        String world = src.getLevel().dimension().location().getPath();
        Origin origin;

        int numArguments = context.getInput().split(" ").length -1;
        if(numArguments == 1){
            String type = context.getArgument("type", String.class);
            origin = new Origin(src.getPosition(), Config.formulas, new String[]{type}, Config.range, false, 2, world);
        }else{
            origin = new Origin(src.getPosition(), Config.formulas, new String[]{}, Config.range, false, 2, world);
        }


        OriginManager.registerOrigin(origin);

        String chatMessage = "Registered new origin\n"
                + "\n"
                + "Located in dimension/s: " + origin.world + "\n"
                + "Position: " + origin.pos.toString() + "\n"
                + "Tags: " + Arrays.toString(origin.tags) + "\n";

        DDGeneralUtility.sendChatMsg(chatMessage, src);

        return 1;
    }


    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("getNearestOriginInfo").requires(commandSource ->
            commandSource.hasPermission(3))
            .executes(DDCommands::getNearestOriginInfo)
                .then(Commands.argument("type", StringArgumentType.word())
                .executes(DDCommands::getNearestOriginInfo)));




        dispatcher.register((Commands.literal("createOrigin").requires(commandSource ->
                commandSource.hasPermission(3))
                .executes(DDCommands::registerNewOrigin))
                    .then(Commands.argument("type", StringArgumentType.word())
                    .executes(DDCommands::registerNewOrigin)));
    }

}
