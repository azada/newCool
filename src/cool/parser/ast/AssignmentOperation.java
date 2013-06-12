package cool.parser.ast;

import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/21/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssignmentOperation extends UnitOperation{

    public AssignmentOperation(ArrayList operands) {
        operandsList = operands;
        this.expType = UNIT_TYPE;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        for (Object operand : operandsList) {
            boolean fml = false;
            try {
                fml = ((Expr) operand).check(pTable);
            } catch (MyException myException) {
                return result;
            }
            result = result && fml;
        }
        if (!Program.isConsistant(((Expr)(operandsList.get(1))).expType,((Expr)(operandsList.get(0))).expType)){
             Program.addError(new MyException("type of expression on the right do not match the expression on the left",this));
            result = false;
        }

        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void generate(StringBuilder builder) {
        /*
        ActivationRecord currentRecord = (ActivationRecord) ActivationStack.getHandle().top();
        Expr firstOp = (Expr) operandsList.get(0);
        firstOp.generate(builder);
        currentRecord.bindToExpr(firstOp);
        CodeGenerator.storeVar(builder, bi);
        CodeGenerator.storeVar();
        Expr secondOp = (Expr) operandsList.get(1);


        firstOp.generate(builder);
        secondOp.generate(builder);
        */
    }

}
