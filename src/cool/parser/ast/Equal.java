package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/20/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Equal extends BooleanOperation {
    public Equal(ArrayList operands) {
        operandsList = operands;
        this.expType = BOOLEAN_TYPE;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        for (Object operand : operandsList) {

            ////////////////////////////////////////////////////////////////////////
            boolean fml = false;
            try {
                fml = ((Expr) operand).check(pTable);
            } catch (MyExeption myExeption) {
                myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            ////////////////////////////////////////////////////////////////////////


            result = result && fml;
        }

        if (((Expr)(operandsList.get(1))).expType.equals(((Expr)(operandsList.get(0))).expType)){
            if (!((Expr)(operandsList.get(1))).expType.equals(INTEGER_TYPE)){
                Program.addError(new MyExeption("one or two sides of the operation is not integer",this));
                result = false;
            }
        }
        else{
            Program.addError(new MyExeption("two sides of the equal operation do not have the same type",this));
            result = false;
        }

        this.expType = BOOLEAN_TYPE;
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
