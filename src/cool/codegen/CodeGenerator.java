package cool.codegen;

import cool.parser.ast.ClassNode;
import cool.parser.ast.Program;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/5/13
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeGenerator {
    public static void openArguments(StringBuilder builder) {
        builder.append("(");
    }

    public static void appendIntPointer(StringBuilder builder) {
       builder.append("i32*");
    }

    public static void appendInt(StringBuilder builder) {
        builder.append("i32");
    }

    //public static void getNewPointer

    public static void createNewObject(StringBuilder builder, ClassNode obj) {

    }

    public static void appendPointer(StringBuilder builder, ClassNode obj) {

    }
}
