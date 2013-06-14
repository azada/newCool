package cool.parser.ast;

import cool.codegen.CodeGenerator;
import cool.exception.MyException;
import cool.symbol.SymbolNode;

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
    public boolean shallowCheck(SymbolNode pTable){
        return true;
    }
    @Override
    public boolean check(SymbolNode pTable) throws MyException {
        //To change body of implemented methods use File | Settings | File Templates.
        boolean result = true;
        try {
            result = result && block.check(pTable);
        } catch (MyException myException) {
            throw myException;
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
        CodeGenerator.comment(builder, "FeatureBlock.generate");
        //ActivationRecord currentRecord = (ActivationRecord)ActivationStack.getHandle().top();
        ArrayList<Expr> exprList = this.block.exprList;
        for (Expr e : exprList) {

           e.generate(builder);
        }
        if (block.end != null ) {
            this.block.end.generate(builder);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
