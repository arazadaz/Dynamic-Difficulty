package com.arazadaz.dd.api.origins;

import com.arazadaz.dd.api.DDContext;
import com.arazadaz.dd.api.Modes.*;
import net.minecraft.world.phys.Vec3;

public class Origin {

    public Vec3 pos;
    private String[] formulas; //(for calculating from it(one for each radius mode), can be specified or uses a default global formula)
    public String[] tags; //Non-unique identifier for the origin(Use modid+tag to allow for specific behavior, otherwise omit your mod id and keep it generic)
    private double range; //How far out formula takes into consideration. Can be specified or uses a default global range
    private boolean noCalculationBound; //Allows for origin to calculate past 1 or 100%

    public Origin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound, int defaultTag){

        String[] newTags;

        //Add default type to origin or none if desired
        if(defaultTag == 1){ //Default tag

            newTags = new String[tags.length+1];
            for(int i = 1; i<=tags.length; i++){
                newTags[i] = tags[i];
            }

            newTags[0] = "default";

        }else if(defaultTag == 2){ //Dynamic tag

            newTags = new String[tags.length+1];
            for(int i = 1; i<=tags.length; i++){
                newTags[i] = tags[i];
            }

            newTags[0] = "dynamic"; //Dynamic because these are generally going to be used for generated origin points after the world was loaded.

        }else{ //No default tag
            newTags = tags; //Keep original tags, exists to prevent conflicts and in case a mod dev wants to specifically design how their origin point interacts with the game.
        }

        this.pos = pos;
        this.formulas = formulas;
        this.tags = newTags;
        this.range = range;
        this.noCalculationBound = noCalculationBound;

    }


    //Bulk of logic goes here
    public double getDifficultyHere(Vec3 pos, DifficultyType type, RadiusMode rMode, DDContext context){

        context.srcOrigin = this;
        context.dstPos = pos;

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
