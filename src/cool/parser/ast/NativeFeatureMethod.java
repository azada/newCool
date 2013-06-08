package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/8/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class NativeFeatureMethod extends Feature{
    String id;
    ArrayList formals;
    String type;

    public NativeFeatureMethod(String id, ArrayList formals, String type) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        symbolNode = new SymbolNode();

    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
