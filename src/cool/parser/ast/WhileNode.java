package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;
import cool.symbol.SymbolTable;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/5/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhileNode extends Expr
{

    Expr condition;
    Expr mainExpr;

    public WhileNode(Expr condition, Expr mainExpr) {
        this.condition = condition;
        this.mainExpr = mainExpr;
        expType = UNIT_TYPE;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;

        ////////////////////////////////////////////////////////////////////////
        boolean co = false;
        boolean ma = false;
        try {
            co = condition.check(pTable);
            ma = mainExpr.check(pTable);
        } catch (MyExeption myExeption) {
            myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ////////////////////////////////////////////////////////////////////////


        return result && co & ma ;

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void accept( ) {
        JSONLogger.openNode("while");
        JSONLogger.openNode("condition");
        condition.accept();
        JSONLogger.closeNode();
        JSONLogger.nextAttribute();
        JSONLogger.openNode("main-expr");
        mainExpr.accept();
        JSONLogger.closeNode();
        JSONLogger.closeNode();

    }
}
