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

    public Extends getExt(){
        return ext;
    }
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


    public int getIndexOf(String var){
        int index = 0 ;

        for (int i = 0 ; i<featureList.size() ; i++) {
            if (featureList.get(i) instanceof FeatureVar){
                if (((FeatureVar)(featureList.get(i))).id.equals(var)){
                    return index;
                }
                else{
                    index++;
                }

            }
        }
        for (int i = 0 ; i<varFormals.size() ; i++){
            if(((FeatureVar)(varFormals.get(i))).id.equals(var)){
               return index;
            }
            index ++;

        }
        return -1;
        
    }


    public int getMethodIndex(String id) {
        int index = varFormals.size();
        if (!type.equals("Any")) {
            index++;
        }
        for (int i=0; i < featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if ( f instanceof FeatureVar) {
                index++;
            }
        }

        for (int i=0; i < featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if ( f instanceof FeatureMethod) {
                if (((FeatureMethod) f).id.equals(id)) {
                    return index;
                } else {
                    index++;
                }
            }
        }
        return -1;
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
        temp1.setClass(type);
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
            temp.setClass(type);
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

    public void generateStructure(StringBuilder builder) {
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

        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureMethod) {
                FeatureMethod fmethod = (FeatureMethod) f;
                if (fmethod instanceof OverrideFeatureMethod) {

                } else {
                    fmethod.generateReference(builder);
                    CodeGenerator.appendComma(builder);
                }
            }

        }

        CodeGenerator.removeExtraComma(builder);
        CodeGenerator.closeBrace(builder);

        CodeGenerator.newLine(builder);

    }

    @Override
    public void generate(StringBuilder builder) {
        CodeGenerator.comment(builder, "ClassNode.generate");
        generateStructure(builder);

        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureMethod) {
                FeatureMethod fmethod = (FeatureMethod) f;
                fmethod.generate(builder);
            }

        }
        generateConstructor(builder);




        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void generateConstructor(StringBuilder builder) {

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


        //super constructor

        ClassNode superNode =  Program.getClassNode(Program.getSuper(type));
        if (this.ext != null) {
            CodeGenerator.generateActuals(builder, this.ext.actuals);
            ArrayList<Integer> args = CodeGenerator.loadActuals(builder, this.ext.actuals);


            int castedMemory = CodeGenerator.castPointer(builder,bindingThis.getLoadedId(), this, superNode);
            builder.append("call void @" + superNode.getConstructorName() + "( " );
            superNode.generateReference(builder);
            builder.append(" %" + castedMemory);
            CodeGenerator.appendComma(builder);
            for (int i=0; i < args.size(); i++ ) {
                String argType = ((Expr)ext.actuals.get(i)).getType();
                Program.getClassNode(argType).generateReference(builder);
                builder.append(" %" + args.get(i) );
                CodeGenerator.appendComma(builder);
            }

            CodeGenerator.removeExtraComma(builder);
            CodeGenerator.closeParen(builder);
            CodeGenerator.newLine(builder);
        }



        //set method pointers



        for (int i=0; i< featureList.size(); i++) {
            Feature f = (Feature) featureList.get(i);
            if (f instanceof FeatureMethod) {
                FeatureMethod fmethod = (FeatureMethod) f;
                int element = -1;
                if (fmethod instanceof OverrideFeatureMethod) {
                    ClassNode origClass = Program.fetchOriginalMethod(type, fmethod.id);
                    FeatureMethod origMethod = Program.lookupMethod(origClass.type, fmethod.id);
                    int origMethodIndex = origClass.getMethodIndex(fmethod.id);
                    int castedMemory = CodeGenerator.castPointer(builder,bindingThis.getLoadedId(), this, superNode);
                    element = CodeGenerator.getElementOf(builder, origClass, castedMemory, origMethodIndex);

                    int castedVar = currentRecord.getNewVariable();
                    builder.append("%" + castedVar + " = bitcast ");
                    fmethod.generateReference(builder);
                    builder.append(" @" +  CodeGenerator.getFlattenName(type, fmethod.id) + " to " );
                    origMethod.generateReference(builder);
                    CodeGenerator.newLine(builder);

                    CodeGenerator.newLine(builder);
                    builder.append("store ");

                    origMethod.generateReference(builder);
                    builder.append(" %" + castedVar + ", " );
                    origMethod.generateReference(builder);
                    builder.append("*");
                    builder.append(" %" + element);
                    builder.append(", align " + getPointerSize());
                    CodeGenerator.newLine(builder);


                } else {
                    int methodIndex = getMethodIndex(fmethod.id);

                    element = CodeGenerator.getElementOf(builder, this,bindingThis.getLoadedId(), methodIndex );

                    CodeGenerator.newLine(builder);
                    builder.append("store ");
                    fmethod.generateReference(builder);
                    builder.append(" @" + CodeGenerator.getFlattenName(type, fmethod.id) + ", " );
                    fmethod.generateReference(builder);
                    builder.append("*");
                    builder.append(" %" + element);
                    builder.append(", align " + getPointerSize());
                    CodeGenerator.newLine(builder);

                }

                   /*
                    int element = currentRecord.getNewVariable();
                    builder.append("%" + element + " = getelementptr inbounds "   );
                    this.generateReference(builder);
                    builder.append(" %" + bindingThis.getLoadedId() + ", i32 0, i32 " + methodIndex );
                     */


            }
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
