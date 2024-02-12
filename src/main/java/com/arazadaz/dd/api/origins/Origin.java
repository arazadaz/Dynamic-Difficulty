package com.arazadaz.dd.api.origins;

import com.arazadaz.dd.Main;
import com.arazadaz.dd.api.DDContext;
import com.arazadaz.dd.api.DifficultyModifier;
import com.arazadaz.dd.api.Modes.*;
import com.arazadaz.dd.core.FormulaInterpreter;
import com.arazadaz.dd.utilities.ShuntingYard;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class Origin {

    public Vec3 pos;
    private String[] formulas; //(for calculating from it(one for each radius mode), can be specified or uses a default global formula)
    public String[] tags; //Non-unique identifier for the origin(Use modid+tag to allow for specific behavior, otherwise omit your mod id and keep it generic)
    private double range; //How far out formula takes into consideration. Can be specified or uses a default global range
    private boolean noCalculationBound; //Allows for origin to calculate past 1 or 100%

    public String world;

    public Origin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound, int defaultTag, String world){

        String[] newTags;

        //Add default type to origin or none if desired
        if(defaultTag == 1){ //Default tag
            newTags = injectDefaultTag(tags, "default");
        }else if(defaultTag == 2){ //Dynamic tag
            newTags = injectDefaultTag(tags, "dynamic"); //Dynamic because these are generally going to be used for generated origin points after the world was loaded.
        }else{ //No default tag
            newTags = tags; //Keep original tags, exists to prevent conflicts and in case a mod dev wants to specifically design how their origin point interacts with the game.
        }

        String[] newRPNFormulas = new String[formulas.length];
        for(int i = 0; i<formulas.length; i++) {
            String newRPNFormula = ShuntingYard.infixToRpn(formulas[i]);
            newRPNFormulas[i] = newRPNFormula;
        }

        this.pos = pos;
        this.formulas = newRPNFormulas;
        this.tags = newTags;
        this.range = range;
        this.noCalculationBound = noCalculationBound;
        this.world = world;

    }

    private String[] injectDefaultTag(String[] originalTags, String tag){

        String[] newTags = new String[originalTags.length+1];
        for(int i = 1; i<=originalTags.length; i++){
            newTags[i] = originalTags[i-1];
        }

        newTags[0] = tag; //On top since it'll be checked most probably

        return newTags;
    }

    private String[] injectTag(String[] originalTags, String tag){

        String[] newTags = new String[originalTags.length+1];
        for(int i = 0; i<=originalTags.length; i++){
            newTags[i] = originalTags[i];
        }

        newTags[originalTags.length] = tag; //On top since it'll be checked most probably

        this.tags = newTags;

        return newTags;

    }

    //TODO add universal tag injector(injects to all origins with predicate condition of type of that origin instance)


    //Bulk of logic goes here
    public double getDifficultyHere(Vec3 dstPos, DifficultyMode type, RadiusMode rMode, DDContext context){

        context.srcOrigin = this;
        context.difficultyType = type;
        context.rMode = rMode;
        context.dstPos = dstPos;

        Iterator<DifficultyModifier> modifierIterator = Main.vault.difficultyModifiers.get(type).iterator();
        double difficulty;

        switch(rMode){

            case CIRCLE -> {
                difficulty = getDifficultyAndRunModifiers(modifierIterator, context, 0, dstPos);
                return getFinalDifficulty(difficulty);
            }

            case SQUARE -> {
                difficulty = getDifficultyAndRunModifiers(modifierIterator, context, 1, dstPos);
                return getFinalDifficulty(difficulty);
            }

            case CUSTOM -> {
                difficulty = getDifficultyAndRunModifiers(modifierIterator, context, 2, dstPos);
                return getFinalDifficulty(difficulty);
            }

            default -> {return 0;} //Should never occur
        }

    }

    private double getFinalDifficulty(double finalDifficulty){
        finalDifficulty = noCalculationBound ? finalDifficulty : Math.min(100, finalDifficulty);
        return finalDifficulty;
    }

    private double getDifficultyAndRunModifiers(Iterator<DifficultyModifier> modifierIterator, DDContext context, int formula, Vec3 dstPos){ //Formula can be 0, 1, or 2 with those values representing circle, square, and custom respectively.
        double base = new FormulaInterpreter().run(this.formulas[formula], this, dstPos);

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

    public double getRange(){
        return this.range;
    }

}
