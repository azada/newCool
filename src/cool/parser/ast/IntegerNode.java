package cool.parser.ast;

import cool.symbol.SymbolNode;
import cool.symbol.SymbolTable;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerNode extends Primary {

    int value;

    public IntegerNode (int value) {
        this.value = value;
        this.expType = INTEGER_TYPE;
    }
    @Override
    public boolean check(SymbolNode pTable) {
        //To change body of implemented methods use File | Settings | File Templates.
        return true;
    }
    @Override
    public void accept( ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
