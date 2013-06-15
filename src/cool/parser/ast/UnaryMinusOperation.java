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
        this.classType = pTable.type;

        /////////////////////////////////////////////////////////////////////////////////
        try {
            temp.check(pTable);
        } catch (MyException myException) {
            myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /////////////////////////////////////////////////////////////////////////////////


        if(!temp.expType.equals(INTEGER_TYPE)){
            result = false;
            Program.addError(new MyException("the expression type for unary minus operation must be an integer not "+ temp.expType ,this));
        }
       return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        ActivationRecord record = ActivationStack.getHandle().top();
        Expr op1 = (Expr)operandsList.get(0);
//        Expr op2 = (Expr)operandsList.get(1);
        op1.generate(builder);
//        op2.generate(builder);

        Binding op1Binding = CodeGenerator.loadExpr(builder, op1,Program.getClassNode(classType));
//        Binding op2Binding = CodeGenerator.loadExpr(builder,op2);
        Binding resultBinding = record.bindToExpr(this);

        ClassNode op1Node = Program.getClassNode(op1.expType);
        CodeGenerator.allocatePointer(builder, resultBinding, op1Node);
        int tempVar = record.getNewVariable();
        builder.append("%" + tempVar + " = " + "sub " );

        op1Node.generateInstance(builder);
        builder.append(" 0 , " + "%" + op1Binding.getLoadedId());
        CodeGenerator.newLine(builder);

        CodeGenerator.storeExpr(builder,tempVar,resultBinding);

        CodeGenerator.newLine(builder);


    }
}
