package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Extends extends Node{

    String type;
    ArrayList actuals;

    boolean native_type;


    public Extends(String type, ArrayList actuals) {
        this.type = type;
        this.actuals = actuals;
    }


    public Extends() {
        this.native_type = true;
    }
    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;



        // now we check this type exists
        if (Program.getInstance().typeClassTable.containsKey(type)){

            ArrayList temp = Program.getInstance().typeClassTable.get(type).varFormals;
            if (actuals.size() != temp.size()){
                Program.addError(new MyExeption("the number of arguments in " + type + " is " + temp.size() + " while " + actuals.size()+" are given",this));
                result = false;
                return result;
            }
            for (Object actual : actuals) {

                /////////////////////////////////////////////////////////////////////////////////
                boolean fml = false;
                try {
                    fml = ((Expr) actual).check(pTable);
                } catch (MyExeption myExeption) {
                    myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                /////////////////////////////////////////////////////////////////////////////////


                result = result && fml;
            }
            for(int i = 0 ; i<actuals.size() ;i++){
                if (((Var)temp.get(i)).type.equals(((Expr)actuals.get(i)).expType))
                    continue;
                else{
                    Program.addError(new MyExeption("types passed to the constructor do not match the required types",this));
                    result = false;
                }
            }
        }
        else{
            Program.addError(new MyExeption("type " + type + " has not been declared",this));
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result;
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("extends");
        JSONLogger.attribute("type", type);

        JSONLogger.closeNode();
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
