package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/12/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BooleanCool extends Primitive {
    public BooleanCool() {
        type = "Boolean";
        featureList = new ArrayList<Feature>();
        varFormals = new ArrayList<Var>();
        ext = null;
        defined = true;
        symbolNode  = new SymbolNode();
        fullyChecked = false;
        symbolNode.type  = new String(type);
        pointerSize = 1;
        //--------------------------------------------
        ArrayList<Formal> formals = new ArrayList<Formal>();
        FeatureMethod f1 = new NativeFeatureMethod("toString",formals,"String");

        featureList.add(f1);
    }

    @Override
    public void generate(StringBuilder builder) {

    }
}
