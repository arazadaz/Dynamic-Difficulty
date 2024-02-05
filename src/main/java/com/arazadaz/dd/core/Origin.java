package com.arazadaz.dd.core;

import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Origin {

    Vec3 pos;
    String[] formulas; //(for calculating from it(one for square and one for circle maybe), can be specified or uses a default formula)
    String[] tags;
    double range; //How far out formula takes into consideration. Can be specified or uses a default global range
    boolean noCalculationBound; //Allows for origin to calculate past 1 or 100%

    public Origin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound){

        this.pos = pos;
        this.formulas = formulas;
        this.tags = tags;
        this.range = range;
        this.noCalculationBound = noCalculationBound;

    }

    public double getDifficultyHere(LivingEntity entity, DifficultyType type, RadiusMode rMode){

        Vec3 pos = entity.position();

        return getDifficultyHere(pos, type, rMode);

    }

    //Bulk of logic goes here
    public double getDifficultyHere(Vec3 pos, DifficultyType type, RadiusMode mode){

        return 0;

    }

    public boolean isWorldSpawnOrigin(){
        return isOriginType("spawn");
    }

    public boolean isOriginType(String tag){

        for(String currentTag : tags){
            if(currentTag.equals(tag)){
                return true;
            }
        }

        return false;

    }

}
