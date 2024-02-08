package com.arazadaz.dd.core;

import com.arazadaz.dd.api.Modes.ModifierMode;
import com.arazadaz.dd.api.origins.Origin;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;

public class FormulaInterpreter {

    private final LinkedList<Double> workingRPNStack = new LinkedList();

    public double run(String formula, Origin origin, Vec3 dstPos){
        String newRPNFormula = ShuntingYard.infixToRpn(formula);
        String[] variables = newRPNFormula.split(" ");

        for(int i = 0; i<variables.length; i++){

            variableDecoder(variables[i], origin, dstPos);

            i++;
        }

        return workingRPNStack.pop();
    }

    private void variableDecoder(String element, Origin origin, Vec3 dstPos){

        if(element.startsWith("max")){
            workingRPNStack.push(Math.max(new FormulaInterpreter().run(element.substring(4, element.indexOf(",")-1), origin, dstPos),
                    new FormulaInterpreter().run(element.substring(element.indexOf(",")+1, element.length()-2) , origin, dstPos)));
        }

        if(element.startsWith("abs")){
            workingRPNStack.push(Math.abs(new FormulaInterpreter().run(element.substring(4, element.length()-2), origin, dstPos)));
        }

        switch (element){

            case "distance" -> {workingRPNStack.push(origin.pos.distanceTo(dstPos));}

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

            default -> {workingRPNStack.push(Double.parseDouble(element));}

        }

    }

    private void calculateRPNValue(ModifierMode modMode){
        double num1 = workingRPNStack.pop();
        double num2 = workingRPNStack.pop();

        switch(modMode){
            case ADDITION -> {
                workingRPNStack.push(num1+num2);
            }
            case SUBSTRACTION -> {
                workingRPNStack.push(num1-num2);
            }
            case MULTIPLY -> {
                workingRPNStack.push(num1*num2);
            }
            case DIVIDE -> {
                workingRPNStack.push(num1/num2);
            }
        }
    }
}
