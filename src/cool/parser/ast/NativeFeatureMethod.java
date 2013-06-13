package cool.parser.ast;

import cool.exception.FatalErrorException;
import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/8/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class NativeFeatureMethod extends FeatureMethod{
    public NativeFeatureMethod(String id, ArrayList formals, String type) {
        super(id,formals,type,null);
    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    public boolean check(SymbolNode pTable){
        boolean result = true;
        this.symbolNode.setParent(pTable);
//        if (Program.typeTableContains(pTable.type)){
//            if (Program.getTableRow(pTable.type).containsKey(id)){
//                Program.addError(new MyException("method "+ this.id + " has duplicate definitions " , this));
//                result = false;
//            }
//            else{
//                Program.getTableRow(pTable.type).put(id, this);
//            }
//        }
//        else{
//            Program.addError(new MyException("the scope for this class has not been defined",this));
//        }

        //we set the parent node to be the pTable

//        for (int i = 0 ; i< formals.size() ; i++){
//            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
//            result = result &&fml;
//        }

        ////////////////////////////////////////////////////////////////
        return result;

    }
    @Override
    public void generate(StringBuilder builder) {


        //To change body of implemented methods use File | Settings | File Templates.
    }
}
