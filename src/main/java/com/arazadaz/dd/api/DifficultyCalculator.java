package com.arazadaz.dd.api;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class DifficultyCalculator {

    public enum RadiusMode {
        CIRCLE,
        SQUARE
    }

    public enum DifficultyType {
        ENVIRONMENTAL,
        SURFACE
    }

    public enum ModifierMode{
        ADDITION,
        SUBSTRACTION,
        MULTIPLY,
        DIVIDE
    }

    public enum Direction{
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    public static double getDifficulty(LivingEntity entity, DifficultyType type, RadiusMode mode){

        switch(mode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }


    public static double getDifficulty(int x, int y, int z, Level level, DifficultyType type, RadiusMode mode){

        switch(mode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }

    public static void addDifficultyModifier(Predicate predicateCondition, DifficultyType type, ModifierMode mode){

        switch(mode){

            case ADDITION -> {}

            case SUBSTRACTION -> {}

            case MULTIPLY -> {}
            case DIVIDE -> {}

            default -> {}
        }

    }

    public static Direction getDirectionNearestOrigin(int x, int y, int z, Level level){


        return null;
    }

}
