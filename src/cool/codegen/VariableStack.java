package cool.codegen;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/8/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableStack {
    private Stack stack;

    private static VariableStack instance = new VariableStack();

    private VariableStack() {
        stack = new Stack();

    }

    public int getVariable() {
       int lastVariable = (Integer)stack.peek();
       lastVariable++;
       stack.push(lastVariable);
       return lastVariable;
    }

    public void startNewStack() {
        stack.push(0);
    }

    public void popCurrentStackVariables() {
        int numberOfVariables = (Integer)stack.peek();
        for (int i = 0; i <= numberOfVariables; i++ ) {
            stack.pop();
        }
    }
    public static VariableStack getHandle() {
        return instance;

    }
}
