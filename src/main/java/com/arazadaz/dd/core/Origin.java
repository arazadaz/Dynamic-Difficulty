package com.arazadaz.dd.core;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Origin {

    Vec3 pos;
    String[] formulas; //(for calculating from it(one for square and one for circle maybe), can be specified or uses a default formula)
    String[] types;
    double range; //How far out formula takes into consideration. Can be specified or uses a default global range
    boolean noCalculationBound; //Allows for origin to calculate past 1 or 100%

    public Origin(Vec3 pos, String[] formulas, String[] types, double range, boolean noCalculationBound){

        this.pos = pos;
        this.formulas = formulas;
        this.types = types;
        this.range = range;
        this.noCalculationBound = noCalculationBound;

    }

    public double getDifficultyHere(LivingEntity entity){

        Vec3 pos = entity.position();

        return getDifficultyHere(pos);

    }

    public double getDifficultyHere(Vec3 pos){

        return 0;

    }

    public boolean isWorldSpawnOrigin(){
        return isOriginType("spawn");
    }

    public boolean isOriginType(String type){

        return false;

    }

}
