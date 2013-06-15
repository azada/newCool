package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.codegen.CodeGenerator;
import cool.exception.MyException;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/5/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class IfNode extends Expr {

    Expr condition;
    Expr mainExpr;
    Expr elseExpr;


    public IfNode(Expr condition, Expr mainExpr, Expr elseExpr) {
        this.condition = condition;
        this.mainExpr = mainExpr;
        this.elseExpr = elseExpr;
    }
    @Override
    public boolean check(SymbolNode pTable) {
        this.classType = pTable.type;
        boolean result = true;
        boolean co,ex ,mx;
        co = ex = mx = true;
        /////////////////////////////////////////////////////////////////////////////////
        try {
            co = condition.check(pTable);
            ex = elseExpr.check(pTable);
            mx = mainExpr.check(pTable);
        } catch (MyException myException) {
            myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /////////////////////////////////////////////////////////////////////////////////

        result = result && co && ex && mx;

        // we should check if the main expr and the elseexp are the same type
        if (elseExpr.expType.equals(mainExpr.expType)){
            this.expType = mainExpr.expType;
        }
        else{
            String temp = Program.mutualParent(elseExpr.expType,mainExpr.expType);
            if (temp != null)
                this.expType = temp;
            else {
                Program.addError(new MyException("the main expression and the else expression do not have a mutual parent" ,this));
                result = false;
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result;
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("if");
        JSONLogger.openNode("cond");
        condition.accept();
        JSONLogger.closeNode();
        JSONLogger.nextAttribute();
        JSONLogger.openNode("main-expr");
        mainExpr.accept();
        JSONLogger.closeNode();
        JSONLogger.nextAttribute();
        JSONLogger.openNode("else-expr");
        elseExpr.accept();
        JSONLogger.closeNode();

        JSONLogger.closeNode();
        //walker.visit(this);
    }


//    @Override
//    public void generate(StringBuilder builder) {
//        ActivationRecord record = ActivationStack.getHandle().top();
//        Binding resultBinding = record.bindToExpr(this);
//
//
//        Expr op1 = condition;
//        op1.generate(builder);
//        Binding op1Binding = CodeGenerator.loadExpr(builder, op1);
//        ClassNode op1Node = Program.getClassNode(op1.expType);
//
//        CodeGenerator.allocatePointer(builder, resultBinding, op1Node);
//
//        // now the result of the condition is stored in
//        Expr op2 = (Expr)operandsList.get(1);
//        op1.generate(builder);
//        op2.generate(builder);
////        Binding op1Binding = CodeGenerator.loadExpr(builder, op1);
//        Binding op2Binding = CodeGenerator.loadExpr(builder,op2);
//
//
//
//
//        int tempVar = record.getNewVariable();
//        builder.append("%" + tempVar + " = " + "icmp eq " );
//
//        op1Node.generateInstance(builder);
//        builder.append(" %" + op1Binding.getLoadedId() + ", " + "%" + op2Binding.getLoadedId());
//        CodeGenerator.newLine(builder);
//
//        int tempVar2 = record.getNewVariable();
//        builder.append("%" + tempVar2 + " = zext i1 %"+ tempVar+" to i8") ;
//        CodeGenerator.newLine(builder);
//
//
//
//        CodeGenerator.storeExpr(builder,tempVar2,resultBinding);
//
//        CodeGenerator.newLine(builder);
//
//
//    }
}
