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
//        FeatureVar f1  = new FeatureVar("array_field");

        ArrayList<Formal> formals1 = new ArrayList<Formal>();
        FeatureMethod f2 = new NativeFeatureMethod("length",formals1,"Int");

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

//        this.featureList.add(f1);
        this.featureList.add(f2);
        this.featureList.add(f3);
        this.featureList.add(f4);
        this.featureList.add(f5);

        //----------------------------------------
    }
}


///** An array is a mutable fixed-size container holding any objects.
// * The elements are numbered from 0 to size-1.
// * An array may be void.  It is not legal to inherit from ArrayAny.
// */
//class ArrayAny(var length : Int) {
//
//        var array_field = native;
//
///** Return length of array. */
//def length() : Int = length;
//
///** Return a new array of size s (the original array is unchanged).
// * Any values in the original array that fit within the new array
// * are copied over.  If the new array is larger than the original array,
// * the additional entries start void.  If the new array is smaller
// * than the original array, entries past the end of the new array are
// * not copied over.
// */
//def resize(s : Int) : ArrayAny = native;
//
//  /* Returns the entry at location index.
//   * precondition: 0 <= index < length()
//   */
//def get(index : Int) : Any = native;
//
//  /* change the entry at location index.
//   * return the old value, if any (or null).
//   * precondition: 0 <= index < length()
//   */
//def set(index : Int, obj : Any) : Any = native;
//}
