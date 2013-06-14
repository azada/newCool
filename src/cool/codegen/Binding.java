package cool.codegen;

import cool.parser.ast.Expr;
import cool.parser.ast.Id;
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
    int loadedId;
    Var var;
    Expr expr;

    public Binding(int llvmVarId, Var var) {
        this.llvmVarId = llvmVarId;
        this.var = var;
        expr = new Id(var.getVarId());
        expr.expType = var.getVarType();

    }

    public void setLoadedId(int loadedId) {
        this.loadedId = loadedId;

    }

    public Binding(int llvmVarId, Expr expr) {
        this.llvmVarId = llvmVarId;
        this.expr = expr;
    }


    public int getLoadedId() {
        return loadedId;
    }

    public int getLLVMId() {
        return llvmVarId;
    }

    public void setLLVMId(int llvmVarId) {
        this.llvmVarId = llvmVarId;
    }
}

