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
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringNode extends Primary {
    String value;

    public StringNode(String value) {
        //System.out.println("StringNode.StringNode");
        this.value = value;
        this.expType = STRING_TYPE;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        //To change body of implemented methods use File | Settings | File Templates.
        this.classType = pTable.type;
        return true;
    }
    @Override
    public void accept( ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        String newid = this.toString();
//        System.out.println("newid = " + newid);
        Binding b = currentRecord.bindToExpr(this);
        CodeGenerator.allocateExpr(builder, b);
        //CodeGenerator.storeInt(builder, b, value);
    }
}
