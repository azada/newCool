package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.exception.MyException;
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


    public String getName() {
        return name;
    }

    public Id(String name) {
        this.name = name;
    }
    @Override
    public boolean check(SymbolNode pTable) throws MyException {
        this.classType = pTable.type;
        boolean result;
        if (name == "this"){
            String tTemp = pTable.lookup("THIS").getType();
            this.expType = tTemp;
            return true;
        }
        SymbolItem temp = pTable.lookup(name);
        if (temp != null){
            this.expType = temp.getType();
            result = true;
        }
        else {
            Program.addError(new MyException("the variable " + name + " has not been declared ",this));
            result = false;

        }
        return result;
        //To change body of implemented methods use File | Settings | File Templates.

    }

    @Override
    public void accept( ) {

    }

    public void generate(StringBuilder builder) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        String newid = this.toString();
//        System.out.println("newid = " + newid);
       /* Binding b = currentRecord.getBindedExpr(name);
        System.out.println("b = " + b);
        if (b == null) {
            builder.append("-------lookup ------\n");
            System.out.println("binding2 = " + b);
        }*/
        // /Binding b = currentRecord.bindToExpr(this);
        //Binding b = currentRecord.lookupBinding(this);
        //CodeGenerator.allocateExpr(builder, b);
        //CodeGenerator.storeExpr(builder,b, value);
    }
}
