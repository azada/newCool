package cool.parser.ast;

import java.util.ArrayList;
import java.util.HashMap;

import cool.codegen.*;
import cool.exception.FatalErrorException;
import cool.exception.MyException;
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
    int pointerSize = 8;
    int size = 0;

    public ClassNode(String type, ArrayList varFormals, Extends ext, ArrayList featureList) {
        this.ext = ext;
        this.type = type;
        this.varFormals = varFormals;
        this.featureList = featureList;
        symbolNode = new SymbolNode();
        symbolNode.type  = new String(type);
        fullyChecked = false;
    }
    public ClassNode() {
    }

    public void increamentSize(int amount){
        size += amount;
    }
    public int getPointerSize() {
        return pointerSize;
    }
    public void calculateSize(){
        // add varformals sizes
        for (int i=0 ; i<varFormals.size(); i++){
            increamentSize(((Var) this.varFormals.get(i)).getSize());
        }
        // add featureVar sizes
        for (Object m : featureList) {
            if(m instanceof FeatureVar){
                increamentSize(((FeatureVar)(m)).getSize());
            }
        }

        // get super and add it's size to yourself.
        if(Program.getSuper(type) != null){
            increamentSize(Program.getClassNode(Program.getSuper(type)).getSize());
        }
    }
    public int getSize(){
        return size;
    }

    public String getType() {
        return type;
    }

    public SymbolNode getSymbolNode() {
        return symbolNode;
    }

    public boolean shallowCheck(SymbolNode pTable) throws FatalErrorException {
        boolean result = true;
        if (Program.typeTableContains(type)){
            Program.addError(new MyException("Class "+ type + " has already been declared" , this));
            defined = false;
            result = false;
        }
        else {
            Program.putTypeTable(type, new HashMap<String, FeatureMethod>());
            Program.putClassTable(type, this);
        }
        return result;
    }
    public boolean featureShallowCheck(SymbolNode pTable){
        boolean result = true;
        for (int i = this.featureList.size()-1 ; i >=0 ; i--){
            /////////////////////////////////////////////////////////////////////////////////
            boolean res = false;
            res = ((Feature)this.featureList.get(i)).shallowCheck(this.symbolNode);
            result = result && res;
        }
        return result;
    }
    @Override
    public boolean check(SymbolNode pTable) throws FatalErrorException {
        boolean result = true;
        SymbolItem temp1 = new SymbolItem("THIS", type,0 , false);
        temp1.setClass(pTable.lookup("THIS").getType());
        symbolNode.insert(temp1);
        for (int i=0 ; i<varFormals.size(); i++){
            boolean vf = ((Var)this.varFormals.get(i)).check(this.symbolNode);
            result = result && vf;
        }
        if(ext != null){
            boolean ex = ext.check(this.symbolNode);
            result = result && ex;
            Program.putInheritance(type, ext.type);
            SymbolItem temp = new SymbolItem("SUPER",ext.type,0, false);
            temp.setClass(pTable.lookup("THIS").getType());
            this.symbolNode.insert(temp);
            // now we set the parent of symbol node of this class to be it's supers symbol node.
            if(Program.classTableContains(ext.type)){
                symbolNode.setParent(Program.getClassNode(ext.type).symbolNode);
            }

        }
        else{
            if (!type.equals("Any")){
                Program.putInheritance(type, "Any");
                this.symbolNode.setParent(null);
            }
        }

        result = result && defined;

        for (int i = this.featureList.size()-1 ; i >=0 ; i--){
            /////////////////////////////////////////////////////////////////////////////////
            boolean res = false;
            try {
                res = ((Feature)this.featureList.get(i)).check(this.symbolNode);
            } catch (FatalErrorException fatal) {
                throw fatal;

            } catch (MyException myException) {
//                myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                //fvar.generate(builder);
                CodeGenerator.appendType(builder,fvar.type);
                CodeGenerator.appendComma(builder);

            }
        }

        for (int i=0; i < varFormals.size(); i++) {
            Var v = (Var) varFormals.get(i);
            //v.generate(builder);
            CodeGenerator.appendType(builder,v.type);
            CodeGenerator.appendComma(builder);
        }
        CodeGenerator.removeExtraComma(builder);
        CodeGenerator.closeBrace(builder);

        CodeGenerator.newLine(builder);

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
        builder.append("@"+flattenName);



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


        ///// body from here




        VariableStack.getHandle().startNewStack();


        //alocate and store arguments

        Var varThis = new Var("this", this.type);
        Binding bindingThis =  currentRecord.bindToNewVariable(varThis);
        CodeGenerator.allocateVar(builder, bindingThis);
        CodeGenerator.storeVar(builder, bindingThis);
        //int newVar = currentRecord.getNewVariable();
        //bindingThis.setLoadedId(newVar);
        CodeGenerator.loadVar(builder, bindingThis);

        if (varFormals.size() > 0) {

            for (int i=0; i < varFormals.size(); i++) {


                Var v = (Var) varFormals.get(i);
                Binding binding = currentRecord.bindToNewVariable(v);
                CodeGenerator.allocateVar(builder, binding );

                CodeGenerator.storeVar(builder,binding);

                //newVar = currentRecord.getNewVariable();
                //binding.setLoadedId(newVar);
                CodeGenerator.loadVar(builder,binding);
                //ClassNode varNode = Program.getClassNode(v.type);

                //CodeGenerator.allocateStack(builder, varNode);
                //v.generate(builder);
                //CodeGenerator.appendComma(builder);
            }
            //CodeGenerator.removeExtraComma(builder);
        }

        //implement feature block

        boolean finish = false;
        for (int i=0; i< featureList.size() && !finish; i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureBlock) {
                FeatureBlock fblock = (FeatureBlock) f;
                fblock.generate(builder);
                finish = true;
            }
        }

        builder.append("ret void");
        CodeGenerator.newLine(builder);

        CodeGenerator.closeBrace(builder);
        CodeGenerator.newLine(builder);
    }

    public void generateInstance(StringBuilder builder) {

        builder.append("%class." + this.type);

    }

    public void generateReference(StringBuilder builder) {
        builder.append("%class."+ this.type + "*" );
    }

    public String getConstructorName() {
        String className = this.type;
        String methodName = "env";
        String flattenName = className + "_" + methodName;
        return flattenName;

    }

    public String getClassPointer() {
        String className = this.type;
        String thisPointer = "%class." + className + "*" + " ";
        return thisPointer;


    }

}
