package cool.symbol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/20/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SymbolNode {
    SymbolNode parent = null;
    ArrayList<SymbolNode> childList ;
    public String type = null;
    public HashMap<String, SymbolItem> symbolTable;

    public SymbolNode(SymbolNode parent) {
        this.parent = parent;
        symbolTable = new HashMap<String, SymbolItem>();
    }

    public SymbolNode() {
        symbolTable = new HashMap<String, SymbolItem>();
    }

    public SymbolItem lookup(String id){
        if (this.symbolTable.containsKey(id)){
            //now we return the symbolItem object
            return this.symbolTable.get(id);
        }
        else{
            if (parent == null)
                return null;
            return parent.lookup(id);
        }
    }
    public void setParent(SymbolNode sn){
        this.parent = sn;
    }
    public void insert(SymbolItem symbolItem){
        symbolTable.put(symbolItem.id, symbolItem);
    }



}
