package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;
import cool.symbol.SymbolTable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Block extends Expr {

    ArrayList exprList;
    SymbolNode symbolNode ;
    Expr end ;
    boolean longInit = true;

    public Block(ArrayList exprList, Expr end) {
        this.exprList = exprList;
        this.end = end;
        symbolNode = new SymbolNode();
    }

    public Block() {
        longInit = false;
    }

    @Override
    public boolean check(SymbolNode pTable){
        // if the block is empty
        if (!longInit){
            this.expType = UNIT_TYPE;
            return true;
        }
        //To change body of implemented methods use File | Settings | File Templates.
        boolean result = true;
        this.symbolNode.setParent(pTable);
        for (int i = 0; i < exprList.size(); i++) {

            /////////////////////////////////////////////////////////////////////////////////
            boolean el = false;
            try {
                el = ((Expr)(exprList.get(i))).check(symbolNode);
            } catch (MyExeption myExeption) {
                myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            /////////////////////////////////////////////////////////////////////////////////

            result = result && el;
        }
        /////////////////////////////////////////////////////////////////////////////////
        boolean ed = false;
        try {
            ed = this.end.check(symbolNode);
        } catch (MyExeption myExeption) {
            myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /////////////////////////////////////////////////////////////////////////////////
        this.expType = end.expType;
        return result && ed;
    }


    @Override
    public void accept() {
        JSONLogger.openListAttribute("block");
        for (int i = 0; i < exprList.size(); i++) {
            JSONLogger.openBrace();
            Expr expr = (Expr) exprList.get(i);
            expr.accept();
            JSONLogger.closeBrace();
            if ( i < (exprList.size() - 1)) {
                JSONLogger.nextAttribute();
            }
        }
        JSONLogger.closeListAttribute();
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
