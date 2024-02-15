package com.arazadaz.dd.commands;

import com.arazadaz.dd.api.DifficultyCalculator;
import com.arazadaz.dd.api.Modes.DifficultyMode;
import com.arazadaz.dd.api.Modes.RadiusMode;
import com.arazadaz.dd.api.origins.Origin;
import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.config.Config;
import com.arazadaz.dd.utilities.DDGeneralUtility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
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
        DifficultyMode difficultyMode = DifficultyMode.SURFACE;
        RadiusMode rMode = RadiusMode.CIRCLE;
        Origin origin;

        int numArguments = context.getInput().split(" ").length -1;
        if(numArguments == 1){
            String tag = context.getArgument("tag", String.class);
            origin = OriginManager.getNearestOrigin(world, tag, src.getPosition());
        }else if(numArguments == 2){
            String tag = context.getArgument("tag", String.class);
            origin = OriginManager.getNearestOrigin(world, tag, src.getPosition());

            String difficultyModeString = context.getArgument("difficulty mode", String.class);
            switch(difficultyModeString){
                case "surface" ->{
                    difficultyMode = DifficultyMode.SURFACE;
                }
                case "environmental" -> {
                    difficultyMode = DifficultyMode.ENVIRONMENTAL;
                }
                case "special" -> {
                    difficultyMode = DifficultyMode.SPECIAL;
                }
            }
        }else if(numArguments == 3){
            String type = context.getArgument("tag", String.class);
            origin = OriginManager.getNearestOrigin(world, type, src.getPosition());

            String difficultyModeString = context.getArgument("difficulty mode", String.class);
            switch(difficultyModeString){
                case "surface" ->{
                    difficultyMode = DifficultyMode.SURFACE;
                }
                case "environmental" -> {
                    difficultyMode = DifficultyMode.ENVIRONMENTAL;
                }
                case "special" -> {
                    difficultyMode = DifficultyMode.SPECIAL;
                }
            }

            String radiusModeString = context.getArgument("radius mode", String.class);
            switch(radiusModeString){
                case "circle" ->{
                    rMode = RadiusMode.CIRCLE;
                }
                case "square" -> {
                    rMode = RadiusMode.SQUARE;
                }
                case "custom" -> {
                    rMode = RadiusMode.CUSTOM;
                }
            }
        }else{
            origin = OriginManager.getNearestOrigin(world, "default", src.getPosition());
        }



        double calculatedDifficulty = DifficultyCalculator.getOriginDifficultyHere(origin, src.getPosition(), difficultyMode, rMode, Optional.empty());
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
            String tag = context.getArgument("tag", String.class);
            origin = new Origin(src.getPosition(), Config.formulas, new String[]{tag}, Config.range, false, 2, world);
        }else if(numArguments == 2){
            String tag = context.getArgument("tag", String.class);
            double range = context.getArgument("range", Double.class);

            origin = new Origin(src.getPosition(), Config.formulas, new String[]{tag}, range, false, 2, world);
        }else if(numArguments == 3){
            String tag = context.getArgument("tag", String.class);
            double range = context.getArgument("range", Double.class);
            boolean noCalcBound = Boolean.parseBoolean(context.getArgument("noCalculationBound", String.class));

            origin = new Origin(src.getPosition(), Config.formulas, new String[]{tag}, range, noCalcBound, 2, world);
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
        dispatcher.register(
                Commands.literal("getNearestOriginInfo")
                .requires(commandSource -> commandSource.hasPermission(3))
                .executes(DDCommands::getNearestOriginInfo)
                .then(
                            Commands.argument("tag", OriginTagArgumentType.tag())
                            .executes(DDCommands::getNearestOriginInfo)
                            .then(
                                        Commands.literal("surface")
                                        .executes(DDCommands::getNearestOriginInfo)
                                        .then(
                                                    Commands.literal("circle")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                    Commands.literal("square")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                    Commands.literal("custom")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                            )
                            .then(
                                        Commands.literal("environmental")
                                        .executes(DDCommands::getNearestOriginInfo)
                                        .then(
                                                    Commands.literal("circle")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                    Commands.literal("square")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                    Commands.literal("custom")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                            )
                            .then(
                                        Commands.literal("special")
                                        .executes(DDCommands::getNearestOriginInfo)
                                        .then(
                                                Commands.literal("circle")
                                                .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                 Commands.literal("square")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                                        .then(
                                                    Commands.literal("custom")
                                                    .executes(DDCommands::getNearestOriginInfo)
                                        )
                            )
                        )
        );




        dispatcher.register(
                Commands.literal("createOrigin")
                .requires(commandSource ->  commandSource.hasPermission(3))
                .executes(DDCommands::registerNewOrigin)
                .then(
                            Commands.argument("tag", StringArgumentType.word())
                            .executes(DDCommands::registerNewOrigin)
                            .then(
                                        Commands.argument("range", DoubleArgumentType.doubleArg(0))
                                        .executes(DDCommands::registerNewOrigin)
                                        .then(
                                                    Commands.argument("noCalculationBound", StringArgumentType.word())
                                                    .executes(DDCommands::registerNewOrigin)
                                            )
                                )
                    )
        );
    }

}
