package cool.codegen;

import cool.codegen.exception.BindingNotFound;
import cool.parser.ast.Expr;
import cool.parser.ast.Id;
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
        map.put(var.getVarId(), binding);
        return binding;
    }

    public Binding getBindedVar(String id) {
        Binding binding = (Binding)map.get(id);
        return binding;
    }

    public Binding bindToExpr(Expr expr) {
        int llvmvar = getNewVariable();
        Binding binding = new Binding(llvmvar, expr);
        map.put(expr.toString(), binding);
        return binding;
    }

    public Binding getBindedExpr(String id) {
        Binding binding = (Binding)map.get(id);
        return binding;
    }

    public Binding lookupBinding(Expr expr) throws BindingNotFound {
        Binding binding = (Binding)map.get(expr.toString());
        return binding;
    }
}
