package com.arazadaz.dd.core;

import com.arazadaz.dd.api.Modes.ModifierMode;
import com.arazadaz.dd.api.origins.Origin;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Stack;

public class FormulaInterpreter {

    private String formula;
    private int variablesIndex = 0;
    private final Stack<Double> workingRPNStack = new Stack<>();

    public double run(String formula, Origin origin, Vec3 dstPos){
        this.formula = formula;
        String[] variables = formula.split(" ");


        int formulaIndex = 0;
        int lastVariablesIndex = 0;
        for(; variablesIndex <variables.length; variablesIndex++, lastVariablesIndex++){

            while(lastVariablesIndex!=variablesIndex){ //If variableIndex is modified by getClosureIndex, correct formulaIndex
                formulaIndex += variables[lastVariablesIndex].length() + 1;
                lastVariablesIndex += 1;
            }

            variableDecoder(variables[variablesIndex], origin, dstPos, formulaIndex);
            formulaIndex += variables[variablesIndex].length() + 1;
        }

        return workingRPNStack.pop();
    }

    private void variableDecoder(String element, Origin origin, Vec3 dstPos, int currentIndex){

        if(element.startsWith("max")){
            int endingIndex = getClosureIndex(currentIndex + 5, '(', ')', true);
            int midIndex = getClosureIndex(currentIndex + 5, (char)0, ',', false);
            double num1 = new FormulaInterpreter().run(formula.substring(currentIndex + 5, midIndex), origin, dstPos);
            double num2 = new FormulaInterpreter().run(formula.substring(midIndex + 2, endingIndex) , origin, dstPos); //+2 to mid(1 for comma, one for extra space)
            double result = Math.max(num1, num2);
            workingRPNStack.push(result);

        }

        else if(element.startsWith("min")){
            int endingIndex = getClosureIndex(currentIndex + 5, '(', ')', true);
            int midIndex = getClosureIndex(currentIndex + 5, (char)0, ',', false);
            double num1 = new FormulaInterpreter().run(formula.substring(currentIndex + 5, midIndex), origin, dstPos);
            double num2 = new FormulaInterpreter().run(formula.substring(midIndex + 2, endingIndex) , origin, dstPos); //+2 to mid(1 for comma, one for extra space)
            double result = Math.min(num1, num2);
            workingRPNStack.push(result);
        }

        else if(element.startsWith("abs")){
            int endingIndex = getClosureIndex(currentIndex + 5, '(', ')', true);
            workingRPNStack.push(Math.abs(new FormulaInterpreter().run(formula.substring(currentIndex + 5, endingIndex), origin, dstPos)));
        }

        else{
            switch (element){

                case "distance" -> {workingRPNStack.push(origin.pos.distanceTo(dstPos));}
                case "range" -> {workingRPNStack.push(origin.getRange());}

                case "src.x" -> {workingRPNStack.push(origin.pos.x);}
                case "src.y" -> {workingRPNStack.push(origin.pos.y);}
                case "src.z" -> {workingRPNStack.push(origin.pos.z);}

                case "dst.x" -> {workingRPNStack.push(dstPos.x);}
                case "dst.y" -> {workingRPNStack.push(dstPos.y);}
                case "dst.z" -> {workingRPNStack.push(dstPos.z);}

                case "+" -> {
                    calculateRPNValue(ModifierMode.ADDITION);
                }
                case "-" -> {
                    calculateRPNValue(ModifierMode.SUBSTRACTION);
                }
                case "*" -> {
                    calculateRPNValue(ModifierMode.MULTIPLY);
                }
                case "/" -> {
                    calculateRPNValue(ModifierMode.DIVIDE);
                }
                case "," -> {
                    variablesIndex ++; //Skip element
                }
                case ")" ->{
                    variablesIndex ++; //skip Element
                }

                default -> {workingRPNStack.push(Double.parseDouble(element));}

            }
        }

    }

    private int getClosureIndex(int startingIndex, char type, char target, boolean adjustCurrentIndex){
        String workingString = formula.substring(startingIndex);

        Iterator<Integer> it = workingString.chars().iterator();

        boolean checkForAdditionClosure = false;
        if(type == (char)0){
            type = '(';
            checkForAdditionClosure = true; //Target Comma wouldn't be within another (
        }

        int closuresNeeded = 0;
        int index = startingIndex;
        while(it.hasNext()){
            char c = (char)it.next().intValue();
            if(c == type){
                closuresNeeded +=1;
            }

            if(c == target || (checkForAdditionClosure && c==')') ){
                if(closuresNeeded == 0){
                    if(adjustCurrentIndex){
                        variablesIndex +=1;
                    }
                    return index;
                }else closuresNeeded--;
            }

            if(adjustCurrentIndex && c == ' '){
                variablesIndex +=1; //
            }


            index++;
        }

        return -1; //Failure
    }

    private void calculateRPNValue(ModifierMode modMode){
        double num1 = workingRPNStack.pop();
        double num2 = workingRPNStack.pop();

        //Stack works in reverse order, so operations may seem flipped

        switch(modMode){
            case ADDITION -> {
                workingRPNStack.push(num2+num1);
            }
            case SUBSTRACTION -> {
                workingRPNStack.push(num2-num1);
            }
            case MULTIPLY -> {
                workingRPNStack.push(num2*num1);
            }
            case DIVIDE -> {
                workingRPNStack.push(num2/num1);
            }
        }
    }
}
