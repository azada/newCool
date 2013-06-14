package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.codegen.CodeGenerator;
import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/20/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlusOperation extends RealOperation {
    public PlusOperation(ArrayList operands) {
        operandsList = operands;
        this.expType = INTEGER_TYPE;
    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        for (Object operand : operandsList) {
            boolean fml = false;

            /////////////////////////////////////////////////////////////////////////////////
            try {
                fml = ((Expr) operand).check(pTable);
            } catch (MyException myException) {
                myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            /////////////////////////////////////////////////////////////////////////////////

            result = result && fml;
        }

        if (((Expr)(operandsList.get(1))).expType.equals(((Expr)(operandsList.get(0))).expType)){
            if (!((Expr)(operandsList.get(1))).expType.equals(INTEGER_TYPE)){
                Program.addError(new MyException("one or two sides of the plus operation is not integer",this));
                result = false;
            }
        }
        else{
            Program.addError(new MyException("two sides of the operation do not have the same type",this));
            result = false;
        }

        this.expType = INTEGER_TYPE;
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        ActivationRecord record = ActivationStack.getHandle().top();
        Expr op1 = (Expr)operandsList.get(0);
        Expr op2 = (Expr)operandsList.get(1);
        op1.generate(builder);
        op2.generate(builder);
        Binding op1Binding = CodeGenerator.loadExpr(builder,op1);
        Binding op2Binding = CodeGenerator.loadExpr(builder,op2);
        Binding resultBinding = record.bindToExpr(this);

        ClassNode op1Node = Program.getClassNode(op1.expType);
        CodeGenerator.allocatePointer(builder, resultBinding, op1Node);
        int tempVar = record.getNewVariable();
        builder.append("%" + tempVar + " = " + "add " );

        op1Node.generateInstance(builder);
        builder.append(" %" + op1Binding.getLoadedId() + ", " + "%" + op2Binding.getLoadedId());
        CodeGenerator.newLine(builder);

        CodeGenerator.storeExpr(builder,tempVar,resultBinding);

        CodeGenerator.newLine(builder);


    }
}
