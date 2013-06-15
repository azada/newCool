package cool.parser.ast;

import cool.exception.MyException;
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

    public ArrayList getActuals(){
        return actuals;
    }

    public Extends() {
        this.native_type = true;
    }
    @Override
    public boolean check(SymbolNode pTable){
        boolean result = true;

        if(!native_type){
            // now we check this type exists
            if (Program.classTableContains(type)){

                ArrayList temp = Program.getClassNode(type).varFormals;
                if (actuals.size() != temp.size()){
                    Program.addError(new MyException("number of arguments in '" + type + "' is " + temp.size() + " - " + actuals.size()+" are given",this));
                    result = false;
                    return result;
                }
                for (Object actual : actuals) {

                    /////////////////////////////////////////////////////////////////////////////////
                    boolean fml = false;
                    try {
                        fml = ((Expr) actual).check(pTable);
                    } catch (MyException myException) {
                        myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    /////////////////////////////////////////////////////////////////////////////////


                    result = result && fml;
                }
                if (result)
                for(int i = 0 ; i<actuals.size() ;i++){
                    if (((Var)temp.get(i)).type.equals(((Expr)actuals.get(i)).expType))
                        continue;
                    else{
                        Program.addError(new MyException("types passed to the constructor do not match the required types",this));
                        result = false;
                    }
                }
            }
            else{
                Program.addError(new MyException("type '" + type + "' has not been declared",this));
            }
        }
        else{
            Program.putInheritance(type,"NATIVE");
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

    @Override
    public void generate(StringBuilder builder) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
