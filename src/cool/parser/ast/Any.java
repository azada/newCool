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

        ArrayList<Formal> formals = new ArrayList<Formal>();
        FeatureMethod f1 = new NativeFeatureMethod("toString",formals,"String");




    }
}

//class Any() extends native {
//
//        /** Returns a string representation for the object */
//        def toString() : String = native;
//
///** return true if this object is equal (in some sense) to the argument */
//def equals(x : Any) : Boolean = native;
//}
