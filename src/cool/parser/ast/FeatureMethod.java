package cool.parser.ast;

import cool.exception.MyExeption;
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

    public FeatureMethod(String id, ArrayList formals, String type, Expr expr ) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.expr = expr;
        symbolNode = new SymbolNode();

    }
    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        this.symbolNode.setParent(pTable);
        if ( Program.typeTableContains(pTable.type)){
            if (Program.getInstance().getTableRow(pTable.type).containsKey(id)){
                Program.addError(new MyExeption("method "+ this.id + " has duplicate definitions " , this));
                result = false;
            }
            else{
                Program.getInstance().getTableRow(pTable.type).put(id, this);
            }
        }
        else{
            Program.addError(new MyExeption("the scope for this class has not been defined",this));
        }

        //we set the parent node to be the pTable

        for (int i = 0 ; i< formals.size() ; i++){
            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
            result = result &&fml;
        }
        boolean express = false;


        ////////////////////////////////////////////////////////////////////////
        try {
            express = expr.check(this.symbolNode);
        } catch (MyExeption myExeption) {
            myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ////////////////////////////////////////////////////////////////////////





        ///////////////////////here we check if we return the correct type in methods ///////////////////////////////
//        System.out.println("expression type is" + expr.expType);
        if(!Program.isConsistant(expr.expType,type)){
            Program.addError(new MyExeption("the return type of method " + id + " should be " + type + " it's not consistant with " + expr.expType ,this));
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
}
