package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/12/13
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Int extends Primitive {
    public Int() {
        this.type = "Int";
        this.featureList = new ArrayList<Feature>();
        this.varFormals = new ArrayList<Var>();
        this.ext = null;
        this.defined = true;
        this.symbolNode  = new SymbolNode();
        this.fullyChecked = false;
        symbolNode.type  = new String(type);
        //--------------------------------------------
        ArrayList<Formal> formals = new ArrayList<Formal>();
        FeatureMethod f1 = new NativeFeatureMethod("toString",formals,"String");
        featureList.add(f1);

    }

    @Override
    public void generate(StringBuilder builder) {

    }
}
