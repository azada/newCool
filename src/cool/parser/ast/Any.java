package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/10/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Any extends ClassNode{
    public Any() {
        this.type = "Any";
        this.featureList = new ArrayList<Feature>();
        this.varFormals = new ArrayList<Var>();
        this.ext = null;
        this.defined = true;
        this.symbolNode  = new SymbolNode();
        this.fullyChecked = false;
        symbolNode.type  = new String(type);

        //----------------------------------------
        ArrayList<Formal> formals = new ArrayList<Formal>();
        FeatureMethod f1 = new NativeFeatureMethod("toString",formals,"String");

        ArrayList<Formal> formals1 = new ArrayList<Formal>();
        formals1.add(new Formal("x", "Any"));
        FeatureMethod f2 = new NativeFeatureMethod("equals",formals1,"Boolean");

        this.featureList.add(f1);
        this.featureList.add(f2);

        //----------------------------------------
    }

}

