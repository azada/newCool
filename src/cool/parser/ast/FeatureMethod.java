package cool.parser.ast;

import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureMethod extends Feature {

    String id;
    ArrayList formals;
    String type;
    Expr expr;
    boolean defined = true;

    public FeatureMethod(String id, ArrayList formals, String type, Expr expr ) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.expr = expr;
        symbolNode = new SymbolNode();

    }

    public boolean shallowCheck(SymbolNode pTable){
        boolean result = true;

        if ( Program.typeTableContains(pTable.type)){
            if (Program.getTableRow(pTable.type).containsKey(id)){
                Program.addError(new MyException("method "+ this.id + " has duplicate definitions " , this));
                this.defined = true;
                result = false;
            }
            else{
                Program.getTableRow(pTable.type).put(id, this);
            }
        }
        else{
            Program.addError(new MyException("the scope for this class has not been defined",this));
        }
        for (int i = 0 ; i< formals.size() ; i++){
            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
            result = result &&fml;
        }
        return result;
    }
    @Override
    public boolean check(SymbolNode pTable) {

        boolean result = true;
        result = result && (!defined);
        this.symbolNode.setParent(pTable);



//        for (int i = 0 ; i< formals.size() ; i++){
//            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
//            result = result &&fml;
//        }
        boolean express = false;


        ////////////////////////////////////////////////////////////////////////
        if (expr == null){
            return true;
        }
        try {
            express = expr.check(this.symbolNode);
        } catch (MyException myException) {

        }

        ///////////////////////here we check if we return the correct type in methods ///////////////////////////////
        if (expr.expType == null){
            return false;
        }
        if(!Program.isConsistent(expr.expType, type)){
            Program.addError(new MyException("the return type of method '" + id + "' should be " + type + " it's not consistant with " + expr.expType ,this));
            result = false;
        }

        ////////////////////////////////////////////////////////////////
        result = result &&  express;
        return result;
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("feature");

        JSONLogger.attribute("id", id);
        JSONLogger.nextAttribute();
        JSONLogger.attribute("type", type);
        JSONLogger.nextAttribute();
        JSONLogger.openListAttribute("formals");
        for ( int i=0; i < formals.size(); i++ ) {
            Formal f = (Formal) formals.get(i);
            JSONLogger.openBrace();
            f.accept();
            JSONLogger.closeBrace();
            if ( i < (formals.size() - 1)) {
                JSONLogger.nextAttribute();
            }
        }
        JSONLogger.closeListAttribute();
        JSONLogger.nextAttribute();
        expr.accept();
        JSONLogger.closeNode();

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        builder.append("define ");
        String className = symbolNode.lookup("THIS").getType();
        String methodName = id;
        String flattenName = className + "_" + methodName;
        String thisPointer = "class." + className + "*" + " %this";


        builder.append(flattenName);

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
