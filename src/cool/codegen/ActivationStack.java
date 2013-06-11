package cool.codegen;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/9/13
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivationStack {
    Stack stack;

    private static ActivationStack instance = new ActivationStack();

    private ActivationStack() {
        stack = new Stack();
    }

    public static ActivationStack getHandle() {
        return instance;
    }


    public void push(ActivationRecord record) {
        stack.push(record);
    }

    public ActivationRecord top() {
        return (ActivationRecord)stack.peek();
    }

    public ActivationStack pop() {
        return (ActivationStack)stack.pop();

    }

    public ActivationRecord startNewActivationRecord() {
        ActivationRecord record = new ActivationRecord();
        push(record);
        return record;
    }

    public void removeCurrentActivationRecord() {
        pop();
    }


}
