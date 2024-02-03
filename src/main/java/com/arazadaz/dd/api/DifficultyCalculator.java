package com.arazadaz.dd.api;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DifficultyCalculator {

    enum Mode{
        CIRCLE,
        SQUARE
    }

    public static double getDifficulty(LivingEntity entity, Mode mode){

        switch(mode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }


    public static double getDifficulty(int x, int y, int z, Level level, Mode mode){

        switch(mode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            default -> {return 0;}
        }


    }

}
