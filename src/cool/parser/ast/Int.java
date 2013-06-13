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
        pointerSize = 4;
        //--------------------------------------------
        ArrayList<Formal> formals = new ArrayList<Formal>();
        FeatureMethod f1 = new NativeFeatureMethod("toString",formals,"String");
        featureList.add(f1);
    }


    @Override
    public void generate(StringBuilder builder) {
//        String temp1 = "\n%class.Int = type { %class.Any, i32}\n";
//        builder.append(temp1);
//
//        String temp2 = "@.str3 = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n" +
//                "\n" +
//                "define i8* @Int_toString(i32 %a) uwtable ssp {\n" +
//                "  %1 = alloca i32, align 4\n" +
//                "  %c = alloca [10 x i8], align 1\n" +
//                "  store i32 %a, i32* %1, align 4\n" +
//                "  %2 = getelementptr inbounds [10 x i8]* %c, i32 0, i32 0\n" +
//                "  %3 = load i32* %1, align 4\n" +
//                "  %4 = call i32 (i8*, i8*, ...)* @sprintf(i8* %2, i8* getelementptr inbounds ([3 x i8]* @.str3, i32 0, i32 0), i32 %3)\n" +
//                "  %5 = getelementptr inbounds [10 x i8]* %c, i32 0, i32 0\n" +
//                "  ret i8* %5\n" +
//                "}\n" +
//                "\n" +
//                "declare i32 @sprintf(i8*, i8*, ...)\n";
//        builder.append(temp2);

    }


    @Override
    public void generateReference(StringBuilder builder) {
        builder.append("i32");
    }

    public void generateInstance(StringBuilder builder) {

        builder.append("i32");

    }
}
