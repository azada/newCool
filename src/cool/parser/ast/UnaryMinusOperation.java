package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/20/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnaryMinusOperation extends UnaryRealOperation {
    public UnaryMinusOperation(ArrayList operands) {
        this.operandsList = operands;
        this.expType = INTEGER_TYPE;
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        Expr temp = ((Expr)(operandsList.get(0)));


        /////////////////////////////////////////////////////////////////////////////////
        try {
            temp.check(pTable);
        } catch (MyExeption myExeption) {
            myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /////////////////////////////////////////////////////////////////////////////////


        if(!temp.expType.equals(INTEGER_TYPE)){
            result = false;
            Program.addError(new MyExeption("the expression type for unary minus operation must be an integer not "+ temp.expType ,this));
        }
       return result;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
