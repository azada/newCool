package cool.parser.ast;

import java.util.ArrayList;
import java.util.HashMap;

import cool.codegen.ActivationRecord;
import cool.codegen.ActivationStack;
import cool.codegen.CodeGenerator;
import cool.codegen.VariableStack;
import cool.exception.FatalErrorException;
import cool.exception.MyExeption;
import cool.symbol.*;
/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassNode extends Node {

    String type;
    Extends ext;
    ArrayList varFormals;
    ArrayList featureList;
    boolean defined = true;
    SymbolNode symbolNode;
    boolean fullyChecked;

    public ClassNode(String type, ArrayList varFormals, Extends ext, ArrayList featureList) {
        this.ext = ext;
        this.type = type;
        this.varFormals = varFormals;
        this.featureList = featureList;
        symbolNode = new SymbolNode();
        symbolNode.type  = new String(type);
        fullyChecked = false;
    }

    public String getType() {
        return type;
    }

    public boolean shallowCheck(SymbolNode pTable){
        boolean result = true;
        if (Program.typeTableContains(type)){
            Program.addError(new MyExeption("Class "+ type + " has already been declared" , this));
            defined = false;
            result = false;
        }
        else {
            Program.typeTablePut(type, new HashMap<String, FeatureMethod>());
            Program.classTablePut(type, this);
        }
        return result;
    }
    @Override
    public boolean check(SymbolNode pTable) throws FatalErrorException {
        boolean result = true;
        SymbolItem temp1 = new SymbolItem("THIS", type,0 , false);
        symbolNode.insert(temp1);
        if(ext != null){
            boolean ex = ext.check(this.symbolNode);
            result = result && ex;
            Program.putInheritance(type, ext.type);
            SymbolItem temp = new SymbolItem("SUPER",ext.type,0, false);
            this.symbolNode.insert(temp);
            // now we set the parent of symbol node of this class to be it's supers symbol node.
            if(Program.classTableContains(ext.type)){
                symbolNode.setParent(Program.getClassNode(ext.type).symbolNode);
            }

        }
        else{
            Program.putInheritance(type, null);
            this.symbolNode.setParent(null);
        }

        result = result && defined;
        for (int i=0 ; i<varFormals.size(); i++){
            boolean vf = ((Var)this.varFormals.get(i)).check(this.symbolNode);
            result = result && vf;
        }

        for (int i = this.featureList.size()-1 ; i >=0 ; i--){
            /////////////////////////////////////////////////////////////////////////////////
            boolean res = false;
            try {
                res = ((Feature)this.featureList.get(i)).check(this.symbolNode);
            } catch (FatalErrorException fatal) {
                throw fatal;

            } catch (MyExeption myExeption) {
                myExeption.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            /////////////////////////////////////////////////////////////////////////////////

             result = result && res;
        }
        fullyChecked = true;
        return result;
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void accept() {
        JSONLogger.openNode("classdecl");
        JSONLogger.attribute("type", type);
        JSONLogger.nextAttribute();
        JSONLogger.openListAttribute("varformals");
        for (int i = 0; i < varFormals.size(); i++) {
            JSONLogger.openBrace();
            Var var = (Var)varFormals.get(i);
            var.accept();
            JSONLogger.closeBrace();
            if (i < (varFormals.size() - 1)) {
                JSONLogger.nextAttribute();
            }
        }
        JSONLogger.closeListAttribute();

        if (ext != null) {
            JSONLogger.nextAttribute();

            ext.accept();
        }
        //JSONLogger.closeListAttribute();
        JSONLogger.nextAttribute();
        JSONLogger.openListAttribute("classbody");
        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            JSONLogger.openBrace();
            f.accept();
            JSONLogger.closeBrace();
            if ( i < (featureList.size() - 1)) {
                JSONLogger.nextAttribute();
            }
        }
        JSONLogger.closeListAttribute();

        JSONLogger.closeNode();
    }

    @Override
    public void generate(StringBuilder builder) {
        System.out.println("ClassNode.generate " + type);
        builder.append("%class." + type + " = " + "type ");
        CodeGenerator.openBrace(builder);
        String parentType = Program.getSuper(this.type);
        ClassNode parentNode = Program.getClassNode(parentType);
        //parentNode.generate(builder);
        if (parentNode != null) {
            parentNode.generateInstance(builder);

            CodeGenerator.appendComma(builder);
        }

        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureVar) {
                FeatureVar fvar = (FeatureVar) f;
                fvar.generate(builder);
                CodeGenerator.appendComma(builder);

            }
        }

        for (int i=0; i < varFormals.size(); i++) {
            Var v = (Var) varFormals.get(i);
            v.generate(builder);
            CodeGenerator.appendComma(builder);
        }
        CodeGenerator.removeExtraComma(builder);
        CodeGenerator.closeBrace(builder);

        generateConstructor(builder);

        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureMethod) {
                FeatureMethod fmethod = (FeatureMethod) f;
                fmethod.generate(builder);
            }

        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void generateConstructor(StringBuilder builder) {

        ActivationRecord currentRecord = ActivationStack.getHandle().startNewActivationRecord();
        builder.append("define ");
        builder.append("void ");


        String flattenName = getConstructorName();
        builder.append(flattenName);



        CodeGenerator.openParen(builder);
        String thisPointer = getClassPointer() + " %this";
        builder.append(thisPointer);
        if (varFormals.size() > 0) {
            CodeGenerator.appendComma(builder);

            for (int i=0; i < varFormals.size(); i++) {
                Var v = (Var) varFormals.get(i);
                v.generate(builder);
                CodeGenerator.appendComma(builder);
            }
        CodeGenerator.removeExtraComma(builder);
        }
        CodeGenerator.closeParen(builder);

        CodeGenerator.newLine(builder);
        CodeGenerator.openBrace(builder);
        CodeGenerator.newLine(builder);


        VariableStack.getHandle().startNewStack();
        if (varFormals.size() > 0) {

            for (int i=0; i < varFormals.size(); i++) {


                Var v = (Var) varFormals.get(i);
                int llvmVar = currentRecord.bindToNewVariable(v.id);
                CodeGenerator.allocateVar(builder, binding);
                ClassNode varNode = Program.getClassNode(v.type);

                CodeGenerator.allocateStack(builder, varNode);
                v.generate(builder);
                CodeGenerator.appendComma(builder);
            }
            CodeGenerator.removeExtraComma(builder);
        }


    }

    public void generateInstance(StringBuilder builder) {
        builder.append("%class." + this.type);

    }

    public void generateReference(StringBuilder builder) {
        builder.append("%class."+ this.type + "* " );
    }

    public String getConstructorName() {
        String className = this.type;
        String methodName = "env";
        String flattenName = className + "_" + methodName;
        return flattenName;

    }

    public String getClassPointer() {
        String className = this.type;
        String thisPointer = "class." + className + "*" + " ";
        return thisPointer;


    }
}
