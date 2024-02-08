package com.arazadaz.dd.api;

import java.util.function.Predicate;

public class DifficultyModifier {
    public Predicate<DDContext> shouldRun;
    public Modes.ModifierMode modMode;
    public double value;

    DifficultyModifier(Predicate<DDContext> predicateCondition, Modes.ModifierMode modMode, double value){
        this.shouldRun = predicateCondition;
        this.modMode = modMode;
        this.value = value;
    }

    public double runModifier(double base){
        switch(modMode) {

            case ADDITION -> {
                base += value;
                return base;
            }

            case SUBSTRACTION -> {
                base -= value;
                return base;
            }

            case MULTIPLY -> {
                base *= value;
                return base;
            }
            case DIVIDE -> {
                base /= value;
                return base;
            }
            case REPLACE -> {
                return value;
            }
        }

        return 0; //Should never occur
    }
}
