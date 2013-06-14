package cool.parser.ast;

import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/12/13
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringCool extends Primitive{
    public StringCool() {
        this.type = "String";
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

        ArrayList<Formal> formals2 = new ArrayList<Formal>();
        formals2.add(new Formal("start", "Int"));
        formals2.add(new Formal("end", "Int"));
        FeatureMethod f2 = new NativeFeatureMethod("substring",formals2,"String");

        featureList.add(f1);
        featureList.add(f2);
    }


//    def substring(start : Int, end : Int) : String = native;

    @Override
    public void generate(StringBuilder builder) {

    }


    @Override
    public void generateReference(StringBuilder builder) {
        builder.append("i8*");
    }

    public void generateInstance(StringBuilder builder) {

        builder.append("i8*");

    }
}
