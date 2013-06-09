package cool.symbol;

import cool.parser.ast.ClassNode;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/21/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SymbolItem {
    String id;
    String type;
    int address;
    boolean init;
    ClassNode classNode;

    public SymbolItem(String id, String type,int address, boolean init, ClassNode classNode) {
        this.id = id;
        this.classNode = classNode;
        this.type = type;
        this.init = init;
        this.address = address;
    }
    public String getType(){
        return type;
    }
}
