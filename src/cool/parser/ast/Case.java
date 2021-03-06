package cool.parser.ast;

import cool.exception.MyException;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/19/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Case extends Expr {
    String id;
    String type;
    Block block;
    boolean longInit;

    public Case(String id, String type, Block block) {
        this.id = id;
        this.type = type;
        this.block = block;
        longInit = true;
    }

    public Case(Block block) {
        this.block = block;
        longInit = false;
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    public boolean check(SymbolNode pTable) throws MyException {
        boolean result = true;
        this.classType = pTable.type;
        if (longInit){
            SymbolItem temp = new SymbolItem(id, type,0, false);
            temp.setClass(pTable.type);
            pTable.insert(temp);
        }
        boolean bl = true;
        try {
            bl = block.check(pTable);
        } catch (MyException myException) {
          throw myException;
        }
        this.expType = block.expType;
        return result && bl;
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
