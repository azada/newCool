package cool.parser.ast;

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
    boolean longInit = false;

    public Case(String id, String type, Block block) {
        this.id = id;
        this.type = type;
        this.block = block;
        longInit = true;
    }

    public Case(Block block) {
        this.block = block;
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        if (longInit){
            SymbolItem temp = new SymbolItem(id, type, false);
            pTable.insert(temp);
        }
        boolean bl = block.check(pTable);
        this.expType = block.expType;
        return result && bl;
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
