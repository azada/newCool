package cool.parser.ast;

import cool.codegen.*;
import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeatureMethod extends Feature {

    String id;
    ArrayList formals;
    String type;
    Expr expr;
    boolean defined = true;
    String classType;

    public FeatureMethod(String id, ArrayList formals, String type, Expr expr ) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.expr = expr;
        symbolNode = new SymbolNode();


    }

    public String getClassType() {
        return classType;
    }

    public boolean shallowCheck(SymbolNode pTable){
        boolean result = true;

        if ( Program.typeTableContains(pTable.type)){
            if (Program.getTableRow(pTable.type).containsKey(id)){
                Program.addError(new MyException("method "+ this.id + " has duplicate definitions " , this));
                this.defined = true;
                result = false;
            }
            else{
                Program.getTableRow(pTable.type).put(id, this);
            }
        }
        else{
            Program.addError(new MyException("the scope for this class has not been defined",this));
        }
        for (int i = 0 ; i< formals.size() ; i++){
            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
            result = result &&fml;
        }
        return result;
    }
    @Override
    public boolean check(SymbolNode pTable) {

        boolean result = true;
        result = result && (!defined);
        this.symbolNode.setParent(pTable);
        classType = pTable.type;


//        for (int i = 0 ; i< formals.size() ; i++){
//            boolean fml = ((Formal)formals.get(i)).check(this.symbolNode);
//            result = result &&fml;
//        }
        boolean express = false;


        ////////////////////////////////////////////////////////////////////////
        if (expr == null){
            return true;
        }
        try {
            express = expr.check(this.symbolNode);
        } catch (MyException myException) {

        }

        ///////////////////////here we check if we return the correct type in methods ///////////////////////////////
        if (expr.expType == null){
            return false;
        }
        if(!Program.isConsistent(expr.expType, type)){
            Program.addError(new MyException("the return type of method '" + id + "' should be " + type + " it's not consistant with " + expr.expType ,this));
            result = false;
        }

        ////////////////////////////////////////////////////////////////
        result = result &&  express;
        return result;
    }
    @Override
    public void accept( ) {
        JSONLogger.openNode("feature");

        JSONLogger.attribute("id", id);
        JSONLogger.nextAttribute();
        JSONLogger.attribute("type", type);
        JSONLogger.nextAttribute();
        JSONLogger.openListAttribute("formals");
        for ( int i=0; i < formals.size(); i++ ) {
            Formal f = (Formal) formals.get(i);
            JSONLogger.openBrace();
            f.accept();
            JSONLogger.closeBrace();
            if ( i < (formals.size() - 1)) {
                JSONLogger.nextAttribute();
            }
        }
        JSONLogger.closeListAttribute();
        JSONLogger.nextAttribute();
        expr.accept();
        JSONLogger.closeNode();

        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generate(StringBuilder builder) {
        builder.append("define ");
        String className = symbolNode.lookup("THIS").getType();
        String methodName = id;
        ClassNode myClass = Program.getClassNode(className);
        String flattenName = CodeGenerator.getFlattenName(className,methodName);
        builder.append(flattenName + "(");
        myClass.generateReference(builder);
        builder.append(" %this");
        CodeGenerator.appendComma(builder);

        if (formals.size() > 0) {

            for (int i=0; i < formals.size(); i++) {
                Var v = ((Formal) formals.get(i)).getVar();
                v.generate(builder);
                CodeGenerator.appendComma(builder);
            }

        }
        CodeGenerator.removeExtraComma(builder);
        CodeGenerator.closeParen(builder);

        CodeGenerator.newLine(builder);
        CodeGenerator.openBrace(builder);
        CodeGenerator.newLine(builder);

        ActivationRecord currentRecord = ActivationStack.getHandle().startNewActivationRecord();

        Var varThis = new Var("this", myClass.type);
        Binding bindingThis =  currentRecord.bindToNewVariable(varThis);
        CodeGenerator.allocateVar(builder, bindingThis);
        CodeGenerator.storeVar(builder, bindingThis);
        //int newVar = currentRecord.getNewVariable();
        //bindingThis.setLoadedId(newVar);


        CodeGenerator.loadVar(builder, bindingThis);
        //ArrayList<Binding> args = new ArrayList<Binding>(formals.size());
        for (int i=0; i < formals.size(); i++) {
                Var v = ((Formal)formals.get(i)).getVar();
                Binding binding = currentRecord.bindToNewVariable(v);
                CodeGenerator.allocateVar(builder, binding );
                CodeGenerator.storeVar(builder,binding);
          //      args.add(binding);
                CodeGenerator.loadVar(builder,binding);
         }




        this.expr.generate(builder);


        Binding resultBinding = CodeGenerator.loadExpr(builder,expr);
        builder.append("ret" + " %" +resultBinding.getLoadedId());



        CodeGenerator.newLine(builder);
        CodeGenerator.closeBrace(builder);
        CodeGenerator.newLine(builder);


        //To change body of implemented methods use File | Settings | File Templates.
    }
}
