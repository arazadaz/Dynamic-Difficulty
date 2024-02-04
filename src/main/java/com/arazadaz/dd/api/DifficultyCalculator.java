package com.arazadaz.dd.api;


import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class DifficultyCalculator {


    public static double getDifficultyHere(LivingEntity entity, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        Vec3 pos = entity.position();
        Level level = entity.level();

        return getDifficultyHere(pos, level, type, rMode);

    }


    public static double getDifficultyHere(Vec3 pos, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        switch(rMode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            case CUSTOM -> {return 0;}

            default -> {return 0;}
        }


    }

    public static double getWorldSpawnDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from spawn origin(returning 0 if disabled)

        return getOriginDifficultyHere(x, y, z, level, type, rMode, "spawn");

    }

    public static double getOriginDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin point of specific type

        return getOriginDifficultyHere(x, y, z, level, type, rMode, "default"); //Spawn is origin 0, even if disabled
    }

    public static double getOriginDifficultyHere(int x, int y, int z, Level level, DifficultyType type, RadiusMode rMode, String origin){ //Calculates from nearest origin point of specific type

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
