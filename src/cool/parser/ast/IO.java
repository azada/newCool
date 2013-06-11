package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/10/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class IO extends  ClassNode{
    public IO() {
        this.type = "IO";
        this.featureList = new ArrayList<Feature>();
        this.varFormals = new ArrayList<Var>();
        this.ext = null;
        this.defined = true;
        this.symbolNode  = new SymbolNode();
        this.fullyChecked = false;
        //------------------------------------------
        ArrayList<Formal> formals = new ArrayList<Formal>();
        formals.add(new Formal("message", "String"));
        FeatureMethod f1 = new NativeFeatureMethod("abort",formals,"Nothing");

        ArrayList<Formal> formals2 = new ArrayList<Formal>();
        formals2.add(new Formal("arg", "String"));
        FeatureMethod f2 = new NativeFeatureMethod("out",formals2,"IO");

        ArrayList<Formal> formals3 = new ArrayList<Formal>();
        formals3.add(new Formal("arg", "Any"));
        FeatureMethod f3 = new FeatureMethod("is_null",formals3,"Boolean",null);

        ArrayList<Formal> formals4 = new ArrayList<Formal>();
        formals4.add(new Formal("arg", "Any"));
        FeatureMethod f4 = new FeatureMethod("out_any",formals4,"IO",null);

        ArrayList<Formal> formals5 = new ArrayList<Formal>();
        formals5.add(new Formal("arg", "String"));
        FeatureMethod f5 = new NativeFeatureMethod("in",formals5,"String");

        ArrayList<Formal> formals6 = new ArrayList<Formal>();
        formals6.add(new Formal("name", "String"));
        FeatureMethod f6 = new NativeFeatureMethod("symbol",formals6,"Symbol");
        //def symbol_name(sym : Symbol) : String = native;

        ArrayList<Formal> formals7 = new ArrayList<Formal>();
        formals7.add(new Formal("sym", "Symbol"));
        FeatureMethod f7 = new NativeFeatureMethod("symbol_name",formals7,"String");

        this.featureList.add(f1);
        this.featureList.add(f2);
        this.featureList.add(f3);
        this.featureList.add(f4);
        this.featureList.add(f5);
        this.featureList.add(f6);
        this.featureList.add(f7);

        //----------------------------------------


    }

}