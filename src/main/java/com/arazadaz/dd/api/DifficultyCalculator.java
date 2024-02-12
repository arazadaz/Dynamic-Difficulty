package com.arazadaz.dd.api;


import com.arazadaz.dd.Main;
import com.arazadaz.dd.api.origins.OriginManager;
import com.arazadaz.dd.api.origins.Origin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Predicate;

public class DifficultyCalculator {

    //Overloaded getters
    public static double getDifficultyHere(LivingEntity entity, DifficultyMode type, RadiusMode rMode){ //Calculates from nearest origin

        Vec3 pos = entity.position();
        Level level = entity.level();

        return getOriginDifficultyHere(pos, level, type, rMode, "default", Optional.of(entity));

    }


    public static double getDifficultyHere(Vec3 pos, Level level, DifficultyMode type, RadiusMode rMode){ //Calculates from nearest origin

        //Needs to be able to gather origins from all worlds and world-specific origins

        return getOriginDifficultyHere(pos, level, type, rMode, "default", Optional.empty()); //default is applied to user-defined origins/spawn automatically. Mods defined origins can apply it too and should unless they have a reason to omit it.

    }

    public static double getWorldSpawnDifficultyHere(Vec3 pos, Level level, DifficultyMode type, RadiusMode rMode){ //Calculates from spawn origin(returning 0 if disabled)

        return getOriginDifficultyHere(pos, level, type, rMode, "spawn", Optional.empty());

    }





    //Primary getter
    public static double getOriginDifficultyHere(Vec3 pos, Level level, DifficultyMode type, RadiusMode rMode, String originTag, Optional<LivingEntity> entity){ //Calculates from nearest origin point of specific type

        String levelID = level.dimension().location().getPath();

        Origin origin = OriginManager.getNearestOrigin(levelID, originTag, pos);

        return origin.getDifficultyHere(pos, type, rMode, new DDContext(entity.orElse(null)));

    }

    public static double getOriginDifficultyHere(Origin origin, Vec3 pos, DifficultyMode type, RadiusMode rMode, Optional<LivingEntity> entity){
        return origin.getDifficultyHere(pos, type, rMode, new DDContext(entity.orElse(null)));
    }



    //Precise control of difficulty with modifiers
    public static void addDifficultyModifier(Predicate<DDContext> predicateCondition, DifficultyMode type, ModifierMode modMode, double value){ //Will register to DDvault

        DifficultyModifier modifier = new DifficultyModifier(predicateCondition, modMode, value);

        Main.vault.difficultyModifiers.get(type).add(modifier);
    }

}
