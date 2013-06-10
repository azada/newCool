package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Var extends Node {
    String id;
    String type;
    private int llvmId;

    public Var( String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getVarId(){
        return this.id;
    }

    public String getVarType() {
        return type;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        if (!Program.typeTableContains(type)){
            Program.addError(new MyExeption("type '" + type + "' has not been defined",this));
            result = false;
        }
        else {
            SymbolItem temp = new SymbolItem(id, type,0, false);
            pTable.insert(temp);
        }
        return result;

        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("var");
        JSONLogger.attribute("id", id);
        JSONLogger.nextAttribute();
        JSONLogger.attribute("type", type);

        //System.out.println("id:" + id + " type:" + type);
        JSONLogger.closeNode();
    }

    @Override
    public void generate(StringBuilder builder) {
        ClassNode varNode = Program.getClassNode(this.type);
        varNode.generateReference(builder);
        builder.append(" %");
        builder.append(this.id);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setLLVMVarId(int llvmId) {
        this.llvmId = llvmId;
    }

    public int getLLVMVarId() {
        return this.llvmId;
    }
}
