package com.arazadaz.dd.api;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.arazadaz.dd.api.Modes.DifficultyType;
import com.arazadaz.dd.api.Modes.RadiusMode;
import com.arazadaz.dd.api.Modes.ModifierMode;
import com.arazadaz.dd.api.Modes.Direction;

import java.util.function.Predicate;

public class DifficultyCalculator {


    public static double getDifficultyHere(LivingEntity entity, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        switch(rMode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }


    public static double getDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        switch(rMode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }

    public static double getWorldSpawnDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from spawn origin(returning 0 if disabled)

        return getOriginDifficultyHere(x, y, z, level, type, rMode, 0); //Spawn is origin 0, even if disabled

    }

    public static double getOriginDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode, int origin){ //Calculates from a specific origin point(Can't imagine a use case for this outside fo spawn, but it exists)

        return 0;
    }

    public static void addDifficultyModifier(Predicate predicateCondition, DifficultyType type, RadiusMode rMode, ModifierMode modMode){

        switch(modMode){

            case ADDITION -> {}

            case SUBSTRACTION -> {}

            case MULTIPLY -> {}
            case DIVIDE -> {}
            case REPLACE -> {}

            default -> {}
        }

    }

    public static Direction getDirectionNearestOrigin(int x, int y, int z, Level level){


        return null;
    }

}
