package com.arazadaz.dd.utilities;


// Java Implementation of Shunting Yard Algorithm Adapted from geeksforgeeks: https://www.geeksforgeeks.org/java-program-to-implement-shunting-yard-algorithm/

// Importing stack class for stacks DS
import java.util.Stack;
// Importing specific character class as
// dealing with only operators and operands
import java.lang.Character;

public class ShuntingYard {

    // Operator having higher precedence
    // value will be returned
    private static int getPrecedence(char c)
    {

        if (c == '+' || c == '-')
            return 1;
        else if (c == '*' || c == '/')
            return 2;
        else if (c == '^')
            return 3;
        else
            return -1;
    }

    // Operator has Left --> Right associativity
    private static boolean hasLeftToRightAssociativity(char c) {
        if (c == '+' || c == '-' || c == '/' || c == '*') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNumOperator(char c) {
        return (hasLeftToRightAssociativity(c)); //Also test against right if it's ever added
    }

    private static String emptyStack(String output, Stack<Character> stack, char c, char prev, int closuresNeeded){
        while (!stack.isEmpty() && stack.peek() != '('){
            output += (output.isEmpty() || prev == ' ') ? stack.pop() : " " + stack.pop();;
        }

        if(closuresNeeded==0){
            output += ' ';
            output += c; //Include the closure(Always if it is comma)
            output += ' '; //Will result in the expression ending with a space if closure is at the end.
        }

        return output;
    }

    private static boolean endsWithVariable(String output){
        if (output.endsWith("distance") || output.endsWith("range") || output.endsWith("src.x") || output.endsWith("src.y") || output.endsWith("src.z") || output.endsWith("dst.x") || output.endsWith("dst.y") || output.endsWith("dst.z")) {
            return true;
        }
        return false;
    }

    // Method converts  given infixto postfix expression
    // to illustrate shunting yard algorithm
    public static String infixToRpn(String expression)
    {
        //Removing spaces and single quotes to work with internally.
        expression = expression.replace(" ", "");
        expression = expression.replace("'", "");

        // Initialising an empty String
        // (for output) and an empty stack
        Stack<Character> stack = new Stack<>();

        // Initially empty string taken
        String output = new String("");

        // Iterating over tokens using inbuilt
        // .length() function
        char prev = ' '; //Previous char
        boolean justPushedOperator = false; //used to know if a digit needs a space separation
        int closuresNeeded = 0;
        for (int i = 0; i < expression.length(); ++i) {
            // Finding character at 'i'th index
            char c = expression.charAt(i);

            if(!output.isEmpty()) prev = output.charAt(output.length()-1); //Getting previous char for analysis

            if(endsWithVariable(output)){
                output += ' ';
                prev = ' ';
            }

            // If the scanned Token is an
            // operand, add it to output
            if (Character.isLetterOrDigit(c)) {

                if(Character.isLetter(c)){
                    if(!output.isEmpty() && (Character.isDigit(prev) || isNumOperator(prev)) ){
                        output += " ";
                    }
                }else if(Character.isDigit(c)){
                    if(!output.isEmpty() && (Character.isLetter(prev) || isNumOperator(prev)) || justPushedOperator){
                        output += " ";
                        justPushedOperator = false;
                    }
                }
                output += c;
            }


            // If the scanned Token is an '.'
            // Add it to output
            else if(c == '.'){
                output += c;
            }

            // If the scanned Token is an '('
            // push it to the stack
            else if (c == '(') {
                String prev3 = output.length()>3 ?output.substring(output.length()-4) : "";
                if(output.length()>=3 && !(prev3.equals("min") || prev3.equals("max") || prev3.equals("abs")) ){
                    output += c;
                    output +=  ' ';
                }else{
                    closuresNeeded += 1;
                }
            }

                // If the scanned Token is an ')' pop and append
                // it to output from the stack until an '(' is
                // encountered
            else if (c == ')' || c == ',') {
                if(closuresNeeded!= 0){
                    output = emptyStack(output, stack, c, prev, closuresNeeded);
                    closuresNeeded--;
                }else{
                    output = emptyStack(output, stack, c, prev, closuresNeeded);
                }
            }

            // If an operator is encountered then taken the
            // further action based on the precedence of the
            // operator

            else {
                while (
                        !stack.isEmpty()
                                && getPrecedence(c)
                                <= getPrecedence(stack.peek())
                                && hasLeftToRightAssociativity(c)) {
                    // peek() inbuilt stack function to
                    // fetch the top element(token)

                    output += (output.isEmpty() || prev == ' ') ? stack.pop() : " " + stack.pop();
                }
                stack.push(c);
                justPushedOperator = true;
            }
        }

        prev = output.charAt(output.length()-1);

        if(!stack.isEmpty() && stack.peek() != '(' && prev == ' '){
            stack.pop();
        }

        // pop all the remaining operators from
        // the stack and append them to output
        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return "This expression is invalid";
            output += " " + stack.pop();
        }
        return output;
    }

}