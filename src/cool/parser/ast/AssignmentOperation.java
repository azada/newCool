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
        this.classType = pTable.type;
        for (Object operand : operandsList) {
            boolean fml = false;
            try {
                fml = ((Expr) operand).check(pTable);
            } catch (MyException myException) {
                return result;
            }
            result = result && fml;
        }
        if (!Program.isConsistent(((Expr) (operandsList.get(1))).expType, ((Expr) (operandsList.get(0))).expType)){
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
        CodeGenerator.comment(builder, "AssignmentOperation.generate");
        System.out.println("AssignmentOperation.generate");
        ActivationRecord record = ActivationStack.getHandle().top();
        Expr op1 = (Expr)operandsList.get(0);
        Id var =  (Id) op1;
        Expr op2 = (Expr)operandsList.get(1);
        op1.generate(builder);
        op2.generate(builder);
        Binding op1Binding = CodeGenerator.loadExpr(builder, op1, Program.getClassNode(classType));
        Binding op2Binding = CodeGenerator.loadExpr(builder, op2, Program.getClassNode(classType));

        if (!var.getType().equals(op2.expType)) {

            int castedMemory = CodeGenerator.castPointer(builder, op2Binding.getLoadedId(), Program.getClassNode(op2.expType), Program.getClassNode(var.getType()));
            op1Binding.setLoadedId(castedMemory);
            op1Binding.setExprType(var.getType());
        }


        CodeGenerator.storeExpr(builder, op2Binding.getLoadedId(), op1Binding);

//        ClassNode op1Node = Program.getClassNode(op1.expType);
//        CodeGenerator.allocatePointer(builder, resultBinding, op1Node);
//        int tempVar = record.getNewVariable();
//        builder.append("%" + tempVar + " = " + "add " );
//
//        op1Node.generateInstance(builder);
//        builder.append(" %" + op1Binding.getLoadedId() + ", " + "%" + op2Binding.getLoadedId());
//        CodeGenerator.newLine(builder);
//
//        CodeGenerator.storeExpr(builder, tempVar, resultBinding);
//
//        CodeGenerator.newLine(builder);

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





        /*  if (this.expr instanceof Id) {
            Id id = (Id)this.expr;
            result = currentRecord.getBindedVar(id.name);
            CodeGenerator.loadVar(builder, result);

        } else{
            result = currentRecord.getBindedExpr(this.expr.toString());
            if (this.expr instanceof Instance) {

            } else {
                CodeGenerator.loadExpr(builder, result);
            }
            //int loadedVar = currentRecord.getNewVariable();
        }
        //result.setLoadedId(loadedVar);
        */

        //CodeGenerator.storeVar();
    }

}
