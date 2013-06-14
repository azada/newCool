package cool.codegen;

import cool.parser.ast.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/5/13
 * Time: 10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeGenerator {

    public static void comment(StringBuilder builder, String str) {
        builder.append("; "+ str);
        newLine(builder);

    }

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

    public static void allocatePointer(StringBuilder builder, Binding resultBinding, ClassNode typeNode) {
        int varNum = resultBinding.getLLVMId();
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        typeNode.generateReference(builder);
        appendComma(builder);
        int size = typeNode.getPointerSize();
        builder.append("align " + size);
        newLine(builder);
    }



    public static void allocateVar(StringBuilder builder, Binding binding) {
        int varNum = binding.llvmVarId;
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        String type = binding.var.getVarType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        appendComma(builder);
        int size = varNode.getPointerSize();
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
        int size = varNode.getPointerSize();
        builder.append("align " + size);
        newLine(builder);
    }


    public static void storeExpr(StringBuilder builder, int sourceVar, Binding targetBinding) {
        builder.append("store ");
        String type = targetBinding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        builder.append(" %" + sourceVar);

        appendComma(builder);
        varNode.generateReference(builder);
        builder.append("* ");
        builder.append("%" + targetBinding.getLLVMId());
        appendComma(builder);
        int size = varNode.getPointerSize();
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
        int size = varNode.getPointerSize();
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
        int size = varNode.getPointerSize();
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
        //builder.append(" ");
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
        ActivationRecord record = ActivationStack.getHandle().startNewActivationRecord();
        builder.append("define i32 @main()");
        newLine(builder);
        openBrace(builder);

        newLine(builder);
        int pointer = record.getNewVariable();
        builder.append("%" + pointer + " = alloca %class.Main, align 8\n");
        builder.append("call void @Main_env( %class.Main* " + "%" + pointer + " )");
        CodeGenerator.newLine(builder);
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
        int size = varNode.getPointerSize();
        builder.append("align " + size);
        newLine(builder);
    }

    public static void allocateInstance(StringBuilder builder, Binding binding, ClassNode instanceNode, ArrayList actuals) {
        //MyClass* m = new Class();

        ActivationRecord record = ActivationStack.getHandle().top();

        //allocate m

        int varNum = binding.llvmVarId;
        builder.append("%" + varNum + " = ");
        builder.append("alloca ");
        String type = binding.expr.getType();
        ClassNode varNode = Program.getClassNode(type);
        varNode.generateReference(builder);
        appendComma(builder);


        //allocate pointer to memory
        int pointerVar = record.getNewVariable();
        builder.append("%" + pointerVar + " = alloca i8*");
        newLine(builder);

        //allocate memory
        int memoryVar = record.getNewVariable();
        builder.append("%" + memoryVar + " = call noalias i8* @_Znwm(i64 "+ instanceNode.getSize() +  ")");
        newLine(builder);

        //cat memory to classpointer
        int castedMemory = record.getNewVariable();
        builder.append( "%" + castedMemory +  " = bitcast i8* %" + memoryVar + "to " +  instanceNode.getClassPointer() );
        newLine(builder);


        //call constrcutor
        String constName = instanceNode.getConstructorName();
        builder.append("invoke void @" + constName + "(" + instanceNode.getClassPointer() + "%" + castedMemory);
        for (int i=0; i < actuals.size(); i++ ) {
            Expr expr = (Expr) actuals.get(i);
            Binding arg = record.bindToExpr(expr);
            allocateExpr(builder, arg);

        }
        //instanceNode.generateActuals();

        builder.append("%");

        int size = varNode.getPointerSize();
        builder.append("align " + size);
        newLine(builder);
        //To change body of created methods use File | Settings | File Templates.
    }


    public static void generateActuals(StringBuilder builder, ArrayList actuals) {
        ActivationRecord record = ActivationStack.getHandle().top();


        for (int i=0; i < actuals.size(); i++ ) {
            Expr expr = (Expr) actuals.get(i);
            expr.generate(builder);
            CodeGenerator.newLine(builder);
            //allocateExpr(builder, arg);

        }
    }

    public static ArrayList<Integer> loadActuals(StringBuilder builder, ArrayList actuals) {

        ActivationRecord record = ActivationStack.getHandle().top();
        ArrayList<Integer> args = new ArrayList<Integer>(actuals.size());
        for (int i=0; i < actuals.size(); i++ ){
            Binding result = null;
            Expr expr = (Expr) actuals.get(i);
            if (expr instanceof Id) {
                Id id = (Id)expr;
                result = record.getBindedVar(id.getName());
                CodeGenerator.loadVar(builder, result);
            } else{
                result = record.getBindedExpr(expr.toString());
                CodeGenerator.loadExpr(builder, result);
            }

            int argid = result.getLoadedId();
            args.add(argid);
        }
        return  args;

    }


    public static String getFlattenName(String className, String methodName) {
        return className + "_" + methodName;

    }

    public static Binding loadExpr(StringBuilder builder, Expr expr){
        ActivationRecord record = ActivationStack.getHandle().top();
        Binding instanceBinding = null;
        if (expr instanceof Id) {
            Id var = (Id) expr;
            instanceBinding = record.getBindedVar(var.getName());
            CodeGenerator.loadVar(builder, instanceBinding);
        } else {
            instanceBinding = record.getBindedExpr(expr.toString());
            CodeGenerator.loadExpr(builder, instanceBinding);
        }
        newLine(builder);
        return instanceBinding;

    }


    public static int castPointer(StringBuilder builder, int sourceVar, ClassNode sourceType, ClassNode targetType ) {
        ActivationRecord record = ActivationStack.getHandle().top();
        int castedVar = record.getNewVariable();
        builder.append("%" + castedVar + " = bitcast ");
        sourceType.generateReference(builder);
        builder.append(" %" + sourceVar + " to " );
        targetType.generateReference(builder);
        CodeGenerator.newLine(builder);
        return castedVar;
    }

    public static int getElementOf(StringBuilder builder, ClassNode instanceNode, int loadedId, int index) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        int element = currentRecord.getNewVariable();
        builder.append("%" + element + " = getelementptr inbounds "   );
        instanceNode.generateReference(builder);
        builder.append(" %" + loadedId + ", i32 0, i32 " + index );

        CodeGenerator.newLine(builder);
        return element;
    }

    public static int loadMethod(StringBuilder builder, FeatureMethod method, int var ) {
        ActivationRecord currentRecord = ActivationStack.getHandle().top();
        int loadedVar = currentRecord.getNewVariable();
        builder.append("%" + loadedVar + " = load ");
        method.generateReference(builder);
        builder.append("* %" + var + ", align " + method.getPointerSize());
        newLine(builder);
        return loadedVar;
    }
}
