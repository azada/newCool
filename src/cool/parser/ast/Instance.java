package cool.parser.ast;

import cool.exception.MyExeption;
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
        if (Program.getInstance().typeTableContains(type)){
            // now that we know such class exists, we check the arguments. we check the actuals
            for (int i=0 ; i<actuals.size(); i++){
                ////////////////////////////////////////////////////////////////////////
                boolean ac = false;
                try {
                    ac = ((Expr)actuals.get(i)).check(pTable);
                } catch (MyExeption myExeption) {
                    myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                ////////////////////////////////////////////////////////////////////////


                result = result && ac;
            }
            // if there is a  problem with the actuals, we return flase and do not check them with varformals.
            if (!result)
                return result;
            // now that actuals have passed the test, we check their type with the varformals of the initialized class
            ArrayList temp = Program.getInstance().typeClassTable.get(type).varFormals;
            //first we check the numbers:
            if(temp.size() != actuals.size()){
                Program.addError(new MyExeption("the number of arguments needed for initializing " + type + " is " + temp.size(),this));
                result = false;
            }
            else{
                // now we know there are the same number, we check their type:
                for(int i = 0 ; i<actuals.size() ;i++){
                    if (((Var)temp.get(i)).type.equals(((Expr)actuals.get(i)).expType))
                        continue;
                    else{
                        Program.addError(new MyExeption("types passed to the constructor do not match the required types",this));
                        result = false;
                    }
                }
            }
        }
        else{
            Program.addError(new MyExeption("the class " + type + "doesn't exist, instance can't be initialized",this));
            result = false;
        }
        //To change body of implemented methods use File | Settings | File Templates.
        return result;
    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
