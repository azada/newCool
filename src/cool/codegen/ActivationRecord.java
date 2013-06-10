package cool.codegen;

import cool.parser.ast.Var;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/9/13
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivationRecord {
    int last_variable = 0;

    HashMap map = new HashMap<String, Binding>();

    public int getNewVariable() {
        last_variable++;
        return last_variable;
    }

    public Binding bindToNewVariable(Var var) {
        int llvmvar = getNewVariable();
        Binding binding = new Binding(llvmvar, var);
        map.put(var.ge, binding);
        return var;
    }

    public int getBindedVar(String id) {
        Binding binding = (Binding)map.get(id);
        return binding.variable;
    }

}
