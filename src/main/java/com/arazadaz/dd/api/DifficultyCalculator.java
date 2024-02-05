package com.arazadaz.dd.api;


import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.api.origins.Origin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Predicate;

public class DifficultyCalculator {

    //Overloaded getters
    public static double getDifficultyHere(LivingEntity entity, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        Vec3 pos = entity.position();
        Level level = entity.level();

        return getOriginDifficultyHere(pos, level, type, rMode, "default", Optional.of(entity));

    }


    public static double getDifficultyHere(Vec3 pos, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from nearest origin

        //Needs to be able to gather origins from all worlds and world-specific origins

        return getOriginDifficultyHere(pos, level, type, rMode, "default", Optional.empty()); //default is applied to user-defined origins/spawn automatically. Mods defined origins can apply it too and should unless they have a reason to omit it.

    }

    public static double getWorldSpawnDifficultyHere(Vec3 pos, Level level, DifficultyType type, RadiusMode rMode){ //Calculates from spawn origin(returning 0 if disabled)

        return getOriginDifficultyHere(pos, level, type, rMode, "spawn", Optional.empty());

    }





    //Primary getter
    public static double getOriginDifficultyHere(Vec3 pos, Level level, DifficultyType type, RadiusMode rMode, String originTag, Optional<LivingEntity> entity){ //Calculates from nearest origin point of specific type

        String levelID = level.toString(); //Will have to debug this to see what value is given.

        Origin originPoint = OriginManager.getNearestOrigin(levelID, originTag, pos);

        return originPoint.getDifficultyHere(pos, type, rMode, new DDContext(entity.orElse(null)));

    }



    //Precise control of difficulty with modifiers
    public static void addDifficultyModifier(Predicate<DDContext> predicateCondition, DifficultyType type, RadiusMode rMode, ModifierMode modMode){ //Will register to DDvault

        switch(modMode){

            case ADDITION -> {}

            case SUBSTRACTION -> {}

            case MULTIPLY -> {}
            case DIVIDE -> {}
            case REPLACE -> {}

            default -> {}
        }

    }

}
