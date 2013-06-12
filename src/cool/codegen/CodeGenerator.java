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
        builder.append("}\n");
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

    public static void allocateVar(StringBuilder builder, Binding binding) {
        int varNum = binding.llvmVarId;
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        String type = binding.var.getVarType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }

    public static void allocateExpr(StringBuilder builder, Binding binding) {
        int varNum = binding.llvmVarId;
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }


    public static void storeExpr(StringBuilder builder, Binding binding) {
        builder.append("store ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append(" %" + binding.llvmVarId);

        appendComma(builder);
        varNode.generateReference(builder);
        builder.append("* ");
        builder.append("%" + binding.llvmVarId);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }

    public static void storeResult(StringBuilder builder, Binding target, Binding source) {
        builder.append("store ");

        String type = source.expr.getType();
        ClassNode exprNode = Program.getClassNode(type);
        exprNode.generateReference(builder);
        builder.append(" %" + source.loadedId);

        appendComma(builder);
        String targetType = target.var.getVarType();
        ClassNode varNode = Program.getClassNode(targetType);
        varNode.generateReference(builder);
        builder.append("* ");
        builder.append("%" + target.llvmVarId);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }

    public static void storeVar(StringBuilder builder, Binding binding) {
        builder.append("store ");
        String type = binding.var.getVarType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append(" %" + binding.var.getVarId() );

        appendComma(builder);
        varNode.generateReference(builder);
        builder.append("* ");
        builder.append("%" + binding.llvmVarId);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }

    public static void loadVar(StringBuilder builder, Binding binding) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        int newVar = currentRecord.getNewVariable();
        binding.setLoadedId(newVar);
        builder.append("%" + binding.loadedId + " = ");
        builder.append("load ");
        String type = binding.var.getVarType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append("* ");

        builder.append("%" + binding.llvmVarId);
        newLine(builder);
    }

    public static void loadExpr(StringBuilder builder, Binding binding) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        int newVar = currentRecord.getNewVariable();
        System.out.println("binding = " + binding);
        binding.setLoadedId(newVar);
        builder.append("%" + binding.loadedId + " = ");
        builder.append("load ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append("* ");

        builder.append("%" + binding.llvmVarId);
        newLine(builder);
    }
    public static void appendType(StringBuilder builder, String type) {
        ClassNode varNode = Program.getClassNode(type);
        if (varNode == null){
            System.out.println(type);
        }
        varNode.generateInstance(builder);
        //To change body of created methods use File | Settings | File Templates.
    }

    public static void appendMain(StringBuilder builder) {
        builder.append("define i32 @main()");
        newLine(builder);
        openBrace(builder);
        newLine(builder);
        builder.append("ret i32\n0");
        newLine(builder);
        closeBrace(builder);
    }

    public static void storeInt(StringBuilder builder, Binding binding, Integer value) {
        builder.append("store ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append( " " + value );

        appendComma(builder);
        varNode.generateReference(builder);
        builder.append("* ");
        builder.append("%" + binding.llvmVarId);
        appendComma(builder);
        int size = varNode.getSize();
        builder.append("align " + size);
        newLine(builder);
    }
}
