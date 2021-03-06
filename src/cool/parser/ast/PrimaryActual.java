package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.codegen.CodeGenerator;
import cool.exception.MyException;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/19/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryActual extends Primary {
    ArrayList actuals;
    Primary primary ;
    String id ;

    public PrimaryActual(ArrayList actuals, Primary primary, String id) {
        this.actuals = actuals;
        this.primary = primary;
        this.id = id;
    }
    // daram taghir midam
    @Override
    public boolean check(SymbolNode pTable) throws MyException {
        // we should check the Type of primary and make sure it has an id with this method.
        boolean result = true;
        this.classType = pTable.type;
        FeatureMethod temp = null;

        boolean pr = primary.check(pTable);
        // first we check if we have this type defined
        if (Program.typeTableContains(primary.expType)){
            // we check if this primary type has this method defined
            if (Program.getTableRow(primary.expType) != null){
                if (!Program.getTableRow(primary.expType).containsKey(id)){
                    // if this Id didn't have this method in itself, we should look up to find this method.
                    if (Program.getSuper(primary.expType)!= null){
                        String superType = Program.getSuper(primary.expType);
                        temp = Program.fetchMethod(superType,id);
                        if(temp == null){
                            // this means that this method doesn't exsist
                            result = false;
                            Program.addError(new MyException("method '" + id + "' doesn't exist within " + primary.expType, this));
                            return false;
                        }
                        else{
                            this.expType = temp.type;
                        }
                    }
                    else{
                        Program.addError(new MyException("method '" + id + "' doesn't exist within " + primary.expType + " or it's supers", this));
                        return false;
                    }

                }
                else{
                    //this method exists within this class
                    temp = Program.getTableRow(primary.expType).get(id);
                    this.expType = temp.type;
                }
            }
            else{
                   Program.addError(new MyException("method '" + id + "' doesn't exist within " + primary.expType, this));
                    return false;
            }

        }
        else{
            // if this primary type has not been defined throw an error
            Program.addError(new MyException("there is no such type '"+ primary.expType + "' defined",this));

            result = false;
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
            if (result)
            for (int i = 0 ; i< temp.formals.size() ; i++){
                if (!Program.isConsistent(((Expr) actuals.get(i)).expType, ((Formal) (temp.formals.get(i))).type)){
                    Program.addError(new MyException("type of actuals doesn't match argument list defined in the method '" + id + "'",this));
                    result = false;
                }
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result && pr;
    }


    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        CodeGenerator.comment(builder, "PrimaryActual.generate");
        ActivationRecord record = ActivationStack.getHandle().top();

        ClassNode instanceNode = Program.getClassNode(this.primary.getType());
        String methodName = id;

        FeatureMethod method = Program.lookupMethod(instanceNode.getType(), methodName);


        if (method instanceof OverrideFeatureMethod) {

        } else {
            //ClassNode returnType = Program.getClassNode(method.type);

            //CodeGenerator.allocatePointer(builder, resultBinding, returnType);


            //CodeGenerator.allocateExpr();
            primary.generate(builder);
            CodeGenerator.generateActuals(builder, this.actuals);



            ClassNode methodNode = Program.getClassNode(method.classType);
            Binding instanceBinding;
            if (method.classType.equals(instanceNode.getType()))  {
                 instanceBinding = CodeGenerator.loadExpr(builder, primary,Program.getClassNode(classType));
            } else {
                instanceBinding = CodeGenerator.loadExpr(builder, primary,Program.getClassNode(classType));

                int castedVar = CodeGenerator.castPointer(builder, instanceBinding.getLoadedId(), instanceNode, methodNode);

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



            ClassNode returnType = Program.getClassNode(method.type);
            int index = methodNode.getMethodIndex(methodName);
            int castedVar = 0;
            if (index < 0) {
                SymbolItem item = methodNode.getSymbolNode().lookup(method.id);
                ClassNode ownerClass = Program.getClassNode(item.getInClass());
                index = ownerClass.getMethodIndex(id);
                castedVar = CodeGenerator.castPointer(builder, instanceBinding.getLoadedId(), instanceNode, ownerClass );


            } else {
                castedVar = instanceBinding.getLoadedId();
            }

            int methodVar = CodeGenerator.getElementOf(builder,methodNode, castedVar, index);
            int loadedMethod = CodeGenerator.loadMethod(builder, method, methodVar);

            int immediateVar = record.getNewVariable();
            builder.append("%" + immediateVar + " = call ");
            returnType.generateReference(builder);
            builder.append(" %" + loadedMethod + " (");
            methodNode.generateReference(builder);
            builder.append( " %" + instanceBinding.getLoadedId()  );


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


            /*
            // old static call

            builder.append("%" + immediateVar + " = "  + "call " );
            ClassNode returnType = Program.getClassNode(method.type);
            returnType.generateReference(builder);
            builder.append(" @" +flatName + "( " );
            methodNode.generateReference(builder);
            builder.append( " %" + instanceBinding.getLoadedId()  );
            CodeGenerator.appendComma(builder);
            for (int i=0; i < args.size(); i++ ) {
                String argType = ((Expr)actuals.get(i)).getType();
                Program.getClassNode(argType).generateReference(builder);
                builder.append(" %" + args.get(i) );
                CodeGenerator.appendComma(builder);
            }
            CodeGenerator.removeExtraComma(builder);
            */


            CodeGenerator.newLine(builder);
            Binding resultBinding = record.bindToExpr(this);

            CodeGenerator.allocatePointer(builder, resultBinding, returnType  );
            CodeGenerator.storeExpr(builder,immediateVar, resultBinding);


            CodeGenerator.newLine(builder);

        }


    }
}
