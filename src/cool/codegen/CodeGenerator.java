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
       builder.append("i32* ");
    }

    public static void appendInt(StringBuilder builder) {
        builder.append("i32 ");
    }

    //public static void getNewPointer

    public static void createNewObject(StringBuilder builder, ClassNode obj) {

    }

    public static void appendPointer(StringBuilder builder, ClassNode obj) {

    }

    public static void appendComma(StringBuilder builder) {
        builder.append(", ");
    }

    public static void removeExtraComma(StringBuilder builder) {
        
        int last = builder.lastIndexOf(",");
        //System.out.println("last = " + last + " len = " + builder.length());
        int len = builder.length();
        boolean finish = false;
        for ( int i=len -1 ; i >= last && last > 0 && !finish; i--) {
            if (builder.charAt(i) == ' '  || builder.charAt(i) == ',' ) {
                builder.deleteCharAt(i);
            } else {
                finish = true;
            }
        }
    }

    public static void closeBrace(StringBuilder builder) {
        builder.append(" }\n");
    }

    public static void openBrace(StringBuilder builder) {
        builder.append("{ ");
    }

    public static void openParen(StringBuilder builder) {
        builder.append("(");
    }

    public static void closeParen(StringBuilder builder) {
        builder.append(")");
    }

    public static void newLine(StringBuilder builder) {
        builder.append("\n");
    }

    public static void allocateStack(StringBuilder builder, ClassNode classNode) {


    }

    public static void appendVar(StringBuilder builder, int variable) {
        builder.append("%" + variable + " = ");

    }

    public static void appendAssign(StringBuilder builder) {

    }
}
