package cool.parser.ast;

import cool.exception.FatalErrorException;
import cool.exception.MyException;
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

    public int getSize(){
        return Program.getClassNode(type).getPointerSize();
    }

    public String getVarId(){
        return this.id;
    }

    public String getVarType() {
        return type;
    }

    @Override
    public boolean check(SymbolNode pTable) throws FatalErrorException {
        boolean result = true;

        if (!Program.typeTableContains(type)){
            Program.addError(new MyException("type '" + type + "' has not been defined",this));
            throw new FatalErrorException("type '" + type + "' has not been defined",this);
        }
        if (pTable.symbolTableContains(id)){
            Program.addError(new MyException("variable '" + id + "' with type '" + type + "' has already been defined",this));
            throw new FatalErrorException("variable '" + id + "' with type '" + type + "' has already been defined" , this);
        }
        if (pTable.lookup(id)!= null){
            Program.addError(new MyException("variable '" + id + "' has already been defined either in super classes or within upper hierarchy",this));
            throw new FatalErrorException("variable '" + id + "' has already been defined either in super classes or within upper hierarchy" , this);
        }

        SymbolItem temp = new SymbolItem(id, type,0, false);
        temp.setClass(pTable.lookup("THIS").getType());
        pTable.insert(temp);

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
