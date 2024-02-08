package com.arazadaz.dd.api.origins;

import com.arazadaz.dd.Main;
import com.arazadaz.dd.api.DDContext;
import com.arazadaz.dd.api.DifficultyModifier;
import com.arazadaz.dd.api.Modes.*;
import com.arazadaz.dd.config.Config;
import com.arazadaz.dd.core.FormulaInterpreter;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

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
        context.difficultyType = type;
        context.rMode = rMode;
        context.dstPos = pos;

        Iterator<DifficultyModifier> modifierIterator = Main.vault.difficultyModifiers.get(type).iterator();
        double difficulty;

        switch(rMode){

            case CIRCLE -> {
                difficulty = runModifiers(modifierIterator, context, 0);
                return difficulty;
            }

            case SQUARE -> {
                difficulty = runModifiers(modifierIterator, context, 1);
                return difficulty;
            }

            case CUSTOM -> {
                difficulty = runModifiers(modifierIterator, context, 2);
                return difficulty;
            }

            default -> {return 0;} //Should never occur
        }

    }

    private double runModifiers(Iterator<DifficultyModifier> modifierIterator, DDContext context, int formula){ //Formula can be 0, 1, or 2 with those values representing circle, square, and custom respectively.
        double base = new FormulaInterpreter().run(Config.formulas[formula], this, pos);

        while(modifierIterator.hasNext()){
            DifficultyModifier modifier = modifierIterator.next();
            if(modifier.shouldRun.test(context)){
                base = modifier.runModifier(base);
            }
        }

        return base;
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
