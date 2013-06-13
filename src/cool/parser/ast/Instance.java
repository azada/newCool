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
        Binding binding = currentRecord.bindToExpr(this);
        CodeGenerator.allocateExpr(builder,binding);
    }
}
