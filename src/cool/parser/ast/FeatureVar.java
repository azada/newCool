package cool.parser.ast;

import cool.exception.FatalErrorException;
import cool.exception.MyException;
import cool.symbol.SymbolItem;
import cool.symbol.SymbolNode;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureVar extends Feature {
    String id;
    Boolean isNative = false;
    String type;
    Expr expr;

    public FeatureVar(String id) {
        this.id = id;
        isNative = true;
    }

    public Var getVar(){
        return new Var(id,type);
    }
    public  FeatureVar(String id, String type, Expr expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;

    }
    @Override
    public boolean check(SymbolNode pTable) throws FatalErrorException {
        boolean result = true;
        if (!isNative){
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
            pTable.insert(temp);

            /////////////////////////////////////////////////////////////////////////////////
            boolean ex = false;
            try {
                ex = expr.check(pTable);
            } catch (MyException myException) {
                myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            /////////////////////////////////////////////////////////////////////////////////


            ///////////////////////here we check if we return the correct type in methods ///////////////////////////////
            if(!expr.expType.equals(type)){
                Program.addError(new MyException("type of the right hand side expression is not " + type ,this));
                result = false;
            }
            ////////////////////////////////////////////////////////////////
            result = result &&  ex;
            //To change body of implemented methods use File | Settings | File Templates.
            return result;
        }
        else{
            return true;
        }
    }
    @Override
    public void accept() {
        JSONLogger.openNode("feature");
        JSONLogger.attribute("id", id);
        JSONLogger.nextAttribute();
        if (isNative) {
            JSONLogger.attribute("native", "true");
        } else {
            JSONLogger.attribute("type", type);
            JSONLogger.nextAttribute();
            expr.accept();
        }
        JSONLogger.closeNode();
        //walker.visit(this);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        ClassNode varNode = Program.getClassNode(this.type);
        varNode.generateReference(builder);
        //builder.append(id);
       // if this.type
       // builder.append("class.")
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
