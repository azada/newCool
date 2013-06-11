package cool.parser.ast;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.symbol.SymbolNode;
import cool.symbol.SymbolTable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureBlock extends Feature {



    Block block;

    public FeatureBlock(Block block) {
        this.block = block;

    }
    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        //To change body of implemented methods use File | Settings | File Templates.
        boolean result = true;
        try {
            result = result && block.check(pTable);
        } catch (MyExeption myExeption) {
            throw myExeption;
        }
        return result;
    }

    @Override
    public void accept( ) {
        JSONLogger.openNode("feature");
        JSONLogger.attribute("type", "block");
        JSONLogger.nextAttribute();
        //JSONLogger.openBrace();
        block.accept();
        //JSONLogger.closeBrace();
        JSONLogger.closeNode();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        //ActivationRecord currentRecord = (ActivationRecord)ActivationStack.getHandle().top();
        ArrayList<Expr> exprList = this.block.exprList;
        for (Expr e : exprList) {

           e.generate(builder);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
