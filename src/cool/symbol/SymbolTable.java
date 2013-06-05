package cool.symbol;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/21/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SymbolTable {
    public HashMap<String, SymbolItem> symbolTable;
    public SymbolTable(){
        symbolTable = new HashMap<String, SymbolItem>();
    }
}
