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
 * Date: 6/4/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Method extends Id {
    ArrayList actuals;

    public Method(String id, ArrayList actuals) {
        super(id);
        this.actuals = actuals;

    }

    @Override
    public boolean check(SymbolNode pTable) throws MyException {
        // we should check the Type of primary and make sure it has a method with this method.
        boolean result = true;
        FeatureMethod temp ;
            // we check if this primary type has this method defined
        String tTemp;
        tTemp = pTable.lookup("THIS").getType();

        // first we check if we have this type defined
        if (Program.typeTableContains(tTemp)){
            // we check if this primary type has this method defined
            if (Program.getTableRow(tTemp) != null){
                if (!Program.getTableRow(tTemp).containsKey(name)){
                    // if this Id didn't have this method in itself, we should look up to find this method.
                    if (pTable.lookup("SUPER")!= null){
                        String superType = pTable.lookup("SUPER").getType();
                        temp = Program.fetchMethod(superType, name);
                        if(temp == null){
                            // this means that this method doesn't exsist
                            result = false;
                            Program.addError(new MyException("method '" + name + "' doesn't exist within " + tTemp, this));
                            return false;
                        }
                        else{
                            this.expType = temp.type;
                        }

                    }
                    else{
                        Program.addError(new MyException("method '" + name + "' doesn't exist within " + tTemp + " or it's supersss", this));
                        return false;
                    }
                }
                else{
                    //this method exists within this class
                    temp = Program.getTableRow(tTemp).get(name);
                    this.expType = temp.type;
                }
            }
            else{
                Program.addError(new MyException("method '" + name + "' doesn't exist within " + tTemp, this));
                return false;
            }

        }
        else{
            // if this primary type has not been defined throw an error
            Program.addError(new MyException("there is no such type '"+ tTemp + "' defined",this));
            System.out.println(name);
            return false;
        }
        // now we should check the actuals
        for (int i=0 ; i<actuals.size(); i++){
            boolean ac = ((Expr)this.actuals.get(i)).check(pTable);
            result = result && ac;
        }
        if (temp != null)  {
            // we should make sure we have the same number of actuals and formals in method call
            if (temp.formals.size() != actuals.size()){
                Program.addError(new MyException(temp.formals.size()+ " number of argument needed and " + actuals.size() + " are given",this));
                result = false;
            }
            //and make sure we have the same type in actuals as we had in feature methods.
            for (int i = 0 ; i< temp.formals.size() ; i++){
                if (!Program.isConsistent(((Expr) actuals.get(i)).expType, ((Formal) (temp.formals.get(i))).type)){
                    Program.addError(new MyException("type of actuals doesn't match argument list defined in the method",this));
                    result = false;
                }
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    /*
    @Override
    public void generate(StringBuilder builder) {
        ActivationRecord record = ActivationStack.getHandle().top();

        ClassNode instanceNode = Program.getClassNode(this.getType());
        String methodName = id;

        FeatureMethod method = Program.lookupMethod(instanceNode.getType(), methodName);


        if (method instanceof OverrideFeatureMethod) {

        } else {
            ClassNode returnType = Program.getClassNode(method.type);
            Binding resultBinding = record.bindToExpr(this);
            //CodeGenerator.allocatePointer(builder, resultBinding, returnType);


            //CodeGenerator.allocateExpr();
            primary.generate(builder);
            CodeGenerator.generateActuals(builder, this.actuals);



            ClassNode methodNode = Program.getClassNode(method.classType);
            Binding instanceBinding;
            if (method.classType.equals(instanceNode.getType()))  {
                instanceBinding = CodeGenerator.loadExpr(builder, primary);
            } else {
                instanceBinding = CodeGenerator.loadExpr(builder, primary);

                int castedVar = CodeGenerator.castPointer(builder, instanceBinding.getLLVMId(), instanceNode, methodNode);

                instanceBinding.setLoadedId(castedVar);
                instanceBinding.setLLVMId(castedVar);
                instanceBinding.setExprType(method.classType);
            }
//
//          Binding instanceBinding = null;
//            if (primary instanceof Id) {
//                Id var = (Id) primary;
//                instanceBinding = record.getBindedVar(var.name);
//                CodeGenerator.loadVar(builder, instanceBinding);
//            } else {
//                instanceBinding = record.getBindedExpr(primary.toString());
//                CodeGenerator.loadExpr(builder, instanceBinding);
//            }
            ArrayList<Integer> args = CodeGenerator.loadActuals(builder, actuals);

            String flatName = CodeGenerator.getFlattenName(method.classType, methodName);





            builder.append("%" + resultBinding.getLLVMId() + " = "  + "call " + flatName + "( " );
            methodNode.generateReference(builder);
            builder.append( " %" + instanceBinding.getLLVMId()  );
            CodeGenerator.appendComma(builder);
            for (int i=0; i < args.size(); i++ ) {
                String argType = ((Expr)actuals.get(i)).getType();
                Program.getClassNode(argType).generateReference(builder);
                builder.append(" %" + args.get(i) );
                CodeGenerator.appendComma(builder);
            }

            CodeGenerator.removeExtraComma(builder);
            CodeGenerator.closeParen(builder);
            CodeGenerator.newLine(builder);

        }

    } */
}
