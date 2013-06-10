package cool.codegen;

import cool.parser.ast.Var;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/9/13
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Binding {
    int llvmVarId;
    Var var;

    public Binding(int llvmVarId, Var var) {
        this.llvmVarId = llvmVarId;
        this.var = var;
    }
}
