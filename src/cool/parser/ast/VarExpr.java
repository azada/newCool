package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.codegen.CodeGenerator;
import cool.exception.FatalErrorException;
import cool.exception.MyExeption;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/8/13
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class VarExpr extends Expr {

    String id;
    String type;
    Expr expr;


    public VarExpr( String id, String type, Expr expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;
        this.expType = UNIT_TYPE;
    }

    public Var getVar(){
        return new Var(id,type);
    }

    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        boolean result = true;

        if (!Program.typeTableContains(type)){
            Program.addError(new MyExeption("type '" + type + "' has not been defined",this));
            throw new FatalErrorException("type '" + type + "' has not been defined",this);
        }
        if (pTable.symbolTableContains(id)){
            Program.addError(new MyExeption("variable '" + id + "' has already been defined",this));
            throw new FatalErrorException("variable '" + id + "' has already been defined" , this);
        }
        if (pTable.lookup(id)!= null){
            Program.addError(new MyExeption("variable '" + id + "' has already been defined either in super classes or within upper hierarchy",this));
            throw new FatalErrorException("variable '" + id + "' has already been defined either in super classes or within upper hierarchy" , this);
        }
        SymbolItem temp = new SymbolItem(id,type,0,false);
        pTable.insert(temp);


        /////////////////////////////////////////////////////////////////////////////////
        boolean ex = false;
        try {
            ex = expr.check(pTable);
        } catch (MyExeption myExeption) {
            throw myExeption;
        }
        /////////////////////////////////////////////////////////////////////////////////

        if(!Program.isConsistant(expr.expType, type)){
            Program.addError(new MyExeption("the type of this expression is not " + type ,this));
            result = false;
        }
        return result && ex;
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("var");
        JSONLogger.attribute("id", id);
        JSONLogger.nextAttribute();
        JSONLogger.attribute("type", type);

        if (expr != null) {
            JSONLogger.nextAttribute();
            expr.accept();
        }

        JSONLogger.closeNode();
    }

    public void generate(StringBuilder builder) {
        ActivationRecord currentRecord = (ActivationRecord) ActivationStack.getHandle().top();
        Var var = this.getVar();
        Binding binding = currentRecord.bindToNewVariable(var);
        CodeGenerator.allocateVar(builder, binding);
        this.expr.generate(builder);

        Binding result = null;
        if (this.expr instanceof Id) {
            Id id = (Id)this.expr;
            result = currentRecord.getBindedVar(id.name);

            CodeGenerator.loadVar(builder, result);
        } else{
            result = currentRecord.getBindedExpr(this.expr.toString());

            CodeGenerator.loadExpr(builder, result);
            //int loadedVar = currentRecord.getNewVariable();
        }
        //result.setLoadedId(loadedVar);

        CodeGenerator.storeResult(builder, binding, result);
        //CodeGenerator.storeVar();
    }
}
