package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Id extends Primary {
    String name;

    public Id(String name) {
        this.name = name;
    }
    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        boolean result;
        SymbolItem temp = pTable.lookup(name);
        if (temp != null){
            this.expType = temp.getType();
            result = true;
        }
        else {
            Program.addError(new MyExeption("the variable " + name + " has not been declared ",this));
            result = false;
            throw new MyExeption("this method doesn't exist", this);

        }
        return result;
        //To change body of implemented methods use File | Settings | File Templates.

    }

    @Override
    public void accept( ) {

    }
}
