package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/10/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayAny extends ClassNode{
    public ArrayAny() {
        this.type = "ArrayAny";
        this.featureList = new ArrayList<Feature>();
        this.varFormals = new ArrayList<Var>();
        this.ext = null;
        this.defined = true;
        this.symbolNode  = new SymbolNode();
        this.fullyChecked = false;
        symbolNode.type  = new String(type);

        //-------------------------------------
        Var v1 = new Var("length","Int");
        this.varFormals.add(v1);
        FeatureVar f1  = new FeatureVar("array_field");

        ArrayList<Formal> formals1 = new ArrayList<Formal>();
        FeatureMethod f2 = new FeatureMethod("length",formals1,"Int",new Id("length"));

        ArrayList<Formal> formals2 = new ArrayList<Formal>();
        formals2.add(new Formal("s", "Int"));
        FeatureMethod f3 = new NativeFeatureMethod("resize",formals2,"ArrayAny");

        ArrayList<Formal> formals3 = new ArrayList<Formal>();
        formals3.add(new Formal("index", "Int"));
        FeatureMethod f4 = new NativeFeatureMethod("get",formals3,"Any");

        ArrayList<Formal> formals4 = new ArrayList<Formal>();
        formals4.add(new Formal("index", "Int"));
        formals4.add(new Formal("obj", "Any"));
        FeatureMethod f5 = new NativeFeatureMethod("set",formals4,"Any");

        this.featureList.add(f1);
        this.featureList.add(f2);
        this.featureList.add(f3);
        this.featureList.add(f4);
        this.featureList.add(f5);

        //----------------------------------------
    }
}
