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
 * Date: 5/19/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Instance extends Primary {
    String type;
    ArrayList actuals;
    public Instance(String type, ArrayList actuals) {
        this.type = type;
        this.actuals = actuals;
        this.expType = type;
    }

//    public Instance(String type) {
//        Type = type;
//    }
    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        // we check the actuals

        // first we chekc if we have this type in our typetable map;
        if (Program.typeTableContains(type)){
            // now that we know such class exists, we check the arguments. we check the actuals
            for (int i=0 ; i<actuals.size(); i++){
                ////////////////////////////////////////////////////////////////////////
                boolean ac = false;
                try {
                    ac = ((Expr)actuals.get(i)).check(pTable);
                } catch (MyException myException) {
                    myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                ////////////////////////////////////////////////////////////////////////


                result = result && ac;
            }
            // if there is a  problem with the actuals, we return flase and do not check them with varformals.
            if (!result)
                return result;
            // now that actuals have passed the test, we check their type with the varformals of the initialized class
            ArrayList temp = Program.getClassNode(type).varFormals;
            //first we check the numbers:
            if(temp.size() != actuals.size() && actuals.size()!= 0){
                Program.addError(new MyException("number of arguments required for initializing " + type + " is " + temp.size(),this));
                result = false;
            }
            else{
                if (actuals.size()!= 0 ){
                // now we know there are the same number, we check their type:
                    for(int i = 0 ; i<actuals.size() ;i++){
                        if (((Var)temp.get(i)).type.equals(((Expr)actuals.get(i)).expType))
                            continue;
                        else{
                            Program.addError(new MyException("types passed to the constructor do not match the required types",this));
                            result = false;
                        }
                    }
                }
            }
        }
        else{
            Program.addError(new MyException("class '" + type + "' doesn't exist, instance can't be initialized",this));
            result = false;
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result;
    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        ClassNode instanceNode = Program.getClassNode(this.type);
        //Binding binding = currentRecord.bindToExpr(this);
        //CodeGenerato  r.allocateInstance(builder,binding, instanceNode, actuals);



        ActivationRecord record = ActivationStack.getHandle().top();

        //allocate m


        //generate arguments
        for (int i=0; i < actuals.size(); i++ ) {
            Expr expr = (Expr) actuals.get(i);
            expr.generate(builder);
            CodeGenerator.newLine(builder);
            //allocateExpr(builder, arg);

        }


        /*int varNum = binding.llvmVarId;
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        CodeGenerator.appendComma(builder);
        */

        //allocate pointer to memory
        int pointerVar = record.getNewVariable();
        builder.append("%" + pointerVar + " = alloca i8*");
        CodeGenerator.newLine(builder);

        //allocate memory
        int memoryVar = record.getNewVariable();
        builder.append("%" + memoryVar + " = call noalias i8* @_Znwm(i64 "+ instanceNode.getSize() +  ")");
        CodeGenerator.newLine(builder);

        //cat memory to classpointer
        int castedMemory = record.bindToExpr(this).getLLVMId(); //getNewVariable();
        builder.append( "%" + castedMemory +  " = bitcast i8* %" + memoryVar + " to " +  instanceNode.getClassPointer() );
        CodeGenerator.newLine(builder);


        //load arguments
        ArrayList<Integer> args = new ArrayList<Integer>(actuals.size());
        for (int i=0; i < this.actuals.size(); i++ ){
            Binding result = null;
            Expr expr = (Expr) actuals.get(i);
            if (expr instanceof Id) {
                Id id = (Id)expr;
                result = currentRecord.getBindedVar(id.name);
                CodeGenerator.loadVar(builder, result);
            } else{
                result = currentRecord.getBindedExpr(expr.toString());
                CodeGenerator.loadExpr(builder, result);
            }

            int argid = result.getLoadedId();
            args.add(argid);
        }


        //call constrcutor
        String constName = instanceNode.getConstructorName();
        builder.append("invoke void @" + constName + "(" );
        instanceNode.generateReference(builder);
        builder.append( " %" + castedMemory);
        CodeGenerator.appendComma(builder);

        for (int i = 0; i < actuals.size(); i++) {
            Expr expr = (Expr) actuals.get(i);

            ClassNode argClass = Program.getClassNode(expr.expType);
            argClass.generateReference(builder);
            builder.append(" %" + args.get(i));
            CodeGenerator.appendComma(builder);
        }

        CodeGenerator.removeExtraComma(builder);
        //record.bindToExpr(this);

        CodeGenerator.closeParen(builder);
        CodeGenerator.newLine(builder);


        /*
        int size = varNode.getPointerSize();
        builder.append("align " + size);
        newLine(builder);*/
    }
}
