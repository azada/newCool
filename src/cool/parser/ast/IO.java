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
        symbolNode.type  = new String(type);
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

//        ArrayList<Formal> formals6 = new ArrayList<Formal>();
//        formals6.add(new Formal("name", "String"));
//        FeatureMethod f6 = new NativeFeatureMethod("symbol",formals6,"Symbol");
//        //def symbol_name(sym : Symbol) : String = native;
//
//        ArrayList<Formal> formals7 = new ArrayList<Formal>();
//        formals7.add(new Formal("sym", "Symbol"));
//        FeatureMethod f7 = new NativeFeatureMethod("symbol_name",formals7,"String");

        this.featureList.add(f1);
        this.featureList.add(f2);
        this.featureList.add(f3);
        this.featureList.add(f4);
        this.featureList.add(f5);
//        this.featureList.add(f6);
//        this.featureList.add(f7);

        //----------------------------------------


    }


    @Override
    public void generate(StringBuilder builder) {
        String temp3 = "%class.IO = type { %class.Any, i32}";
        builder.append(temp3);

        String temp = "" +"@.str = private unnamed_addr constant [3 x i8] c\"%d\00\", align 1" +
                "define void @IO_out(%class.IO* %this, i32 %a) uwtable ssp {\n" +
                "  %1 = alloca i32, align 4\n" +
                "  store i32 %a, i32* %1, align 4\n" +
                "  %2 = load i32* %1, align 4\n" +
                "  %3 = call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([3 x i8]* @.str, i32 0, i32 0), i32 %2)\n" +
                "  ret void\n" +
                "}\n" +
                "\n" +
                "declare i32 @printf(i8*, ...)\n" +
                "\n";
        builder.append(temp);
        String temp2 = "define zeroext i1 @IO_is_null(%class.IO* %this ,%class.Any* %a) nounwind uwtable ssp {\n" +
                "  %1 = alloca i1, align 1\n" +
                "  %2 = alloca %class.Any*, align 8\n" +
                "  store %class.Any* %a, %class.Any** %2, align 8\n" +
                "  %3 = load %class.Any** %2, align 8\n" +
                "  %4 = icmp eq %class.Any* %3, null\n" +
                "  br i1 %4, label %5, label %6\n" +
                "\n" +
                "; <label>:5                                       ; preds = %0\n" +
                "  store i1 true, i1* %1\n" +
                "  br label %7\n" +
                "\n" +
                "; <label>:6                                       ; preds = %0\n" +
                "  store i1 false, i1* %1\n" +
                "  br label %7\n" +
                "\n" +
                "; <label>:7                                       ; preds = %6, %5\n" +
                "  %8 = load i1* %1\n" +
                "  ret i1 %8\n" +
                "}"  ;
        builder.append(temp2);
    }
}


///** The IO class provides simple input and output operations */
//class IO() {
//
//        /** Terminates program with given message.
//         * Return type of native means that
//         * (1) result type is compatible with anything
//         * (2) function will not return.
//         */
//        def abort(message : String) : Nothing = native;
//
///** Print the argument (without quotes) to stdout and return itself */
//def out(arg : String) : IO = native;
//
//def is_null(arg : Any) : Boolean = {
//        arg match {
//        case null => true
//        case x:Any => false
//        }
//        };
//
///** Convert to a string and print */
//def out_any(arg : Any) : IO = {
//        out(if (is_null(arg)) "null" else arg.toString())
//        };
//
///** Read and return characters from stdin to the next newline character.
// * Return null on end of file.
// */
//def in() : String = native;
//
///** Get the symbol for this string, creating a new one if needed. */
//def symbol(name : String) : Symbol = native;
//
///** Return the string associated with this symbol. */
//def symbol_name(sym : Symbol) : String = native;
//}