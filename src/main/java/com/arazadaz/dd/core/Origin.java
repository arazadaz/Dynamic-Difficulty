package com.arazadaz.dd.core;

import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Origin {

    public Vec3 pos;
    private String[] formulas; //(for calculating from it(one for square and one for circle maybe), can be specified or uses a default formula)
    public String[] tags;
    private double range; //How far out formula takes into consideration. Can be specified or uses a default global range
    private boolean noCalculationBound; //Allows for origin to calculate past 1 or 100%

    public Origin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound){

        this.pos = pos;
        this.formulas = formulas;
        this.tags = tags;
        this.range = range;
        this.noCalculationBound = noCalculationBound;

    }


    //Bulk of logic goes here
    public double getDifficultyHere(Vec3 pos, DifficultyType type, RadiusMode rMode){

        switch(rMode){

            case CIRCLE -> {return 0;}

            case SQUARE -> {return 0;}

            case CUSTOM -> {return 0;}

            default -> {return 0;}
        }

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
