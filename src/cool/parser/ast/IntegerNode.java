package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.Binding;
import cool.codegen.CodeGenerator;
import cool.symbol.SymbolNode;
import cool.symbol.SymbolTable;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegerNode extends Primary {

    int value;

    public IntegerNode (int value) {
        this.value = value;
        this.expType = INTEGER_TYPE;
    }
    @Override
    public boolean check(SymbolNode pTable) {
        //To change body of implemented methods use File | Settings | File Templates.
        return true;
    }
    @Override
    public void accept( ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    public void generate(StringBuilder builder) {
        ActivationRecord currentRecord = (ActivationRecord) ActivationStack.getHandle().top();
        String newid = this.toString();
        System.out.println("newid = " + newid);
        Binding b = currentRecord.bindToExpr(this);
        CodeGenerator.allocateExpr(builder, b);
        CodeGenerator.storeInt(builder,b, value);

    }
}
