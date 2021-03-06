package cool;

import beaver.Parser;
import beaver.Scanner;
import beaver.Symbol;
import cool.codegen.CodeGenerator;
import cool.exception.FatalErrorException;
import cool.exception.MyException;
import cool.parser.SimpleParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cool.parser.ast.*;



/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 4/10/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyCoolParser {

    Scanner myCoolScanner = null;

    public MyCoolParser(InputStream inputStream) {
        myCoolScanner = new MyCoolScanner(inputStream);
    }
    public Symbol getToken() {

        try {
            return myCoolScanner.nextToken();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Scanner.Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void parse2() throws Parser.Exception {
        SimpleParser parser = new SimpleParser();
        try {
            ArrayList classList = (ArrayList) parser.parse(myCoolScanner);
            classList.add(new IO());
            classList.add(new ArrayAny());
            classList.add(new Any());
            classList.add(new Int());
            classList.add(new BooleanCool());
            classList.add(new StringCool());
            Program.setClasses(classList);

//            JSONLogger.openBrace();
//            JSONLogger.openListAttribute("program");
//            for (int i = 0; i < classList.size(); i++) {
//                JSONLogger.openBrace();
//                ClassNode node = (ClassNode) classList.get(i);
//                node.accept();
//                JSONLogger.closeBrace();
//                if (i < (classList.size() - 1)) {
//                    JSONLogger.nextAttribute();
//                }
//
//            }
//            JSONLogger.closeListAttribute();
//            JSONLogger.closeBrace();
            System.out.println();


        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    public boolean shallowChecker(){
        boolean result = true;
        for(int i = Program.getClasses().size()-1 ; i>=0 ; i--){
            boolean cn = false;
            try {
                cn = (((ClassNode) Program.getClasses().get(i)).shallowCheck(null));
            } catch (FatalErrorException e) {
                Program.addError(new MyException("Fatal Error Occurred ",e.getErrorInfo()));
                return false;
            }
            result = result && cn;
        }
        return result;
    }
    public boolean methodShallowChecker(){
        boolean result = true;
        for(int i = Program.getClasses().size()-1 ; i>=0 ; i--){
            boolean cn = false;
            cn = (((ClassNode) Program.getClasses().get(i)).featureShallowCheck(null));
            result = result && cn;
        }
        return result;
    }
    public boolean checker(){
        boolean result = shallowChecker();
        boolean sc = methodShallowChecker();
        result = result && sc;

        for(int i = Program.getClasses().size()-1 ; i>=0 ; i--){

            boolean cn = false;
            try {
                cn = (((ClassNode) Program.getClasses().get(i)).check(Program.getSymbolNode()));
            } catch (FatalErrorException fatal) {
                Program.addError(new MyException("Fatal Error Occurred ",fatal.getErrorInfo()));
                return false;
            }
            result = result && cn;
        }
        return result;
    }
    public void calculateSize(){
        for (Object c : Program.getClasses()) {
            ((ClassNode)(c)).calculateSize();
        }
    }

    /*public void parse() {
        Symbol token = getToken();
        while (token.getId() != 0) {
            while (token.getId() > 0) {
                int line =Symbol.getLine(token.getStart());
                int endcol = Symbol.getColumn(token.getEnd());
                //System.out.println("endcol = " + endcol);
                int len = token.getEnd() - token.getStart();
                if (token.getId() == Terminals.STRING) {
                    len = ((String)token.value). length() + 2;
                }
                //System.out.println("len = " + len);
                int start = endcol - len;
                //System.out.println("start = " + start);

                System.out.print(Terminals.NAMES[token.getId()] + " " + line + " " + start + " ");
                if (token.value != null ) {
                   if (token.value.getClass() == String.class ) {
                       String s = (String) token.value;
                       char[] ar = s.toCharArray();
                       String fs = "";
                       for (int i = 0; i < ar.length; i++) {
                           char c = ar[i];
                           switch (c) {
                               case '\0':
                                   fs += "\\0";
                                   break;
                               case '\n':
                                   fs += "\\n";
                                    break;
                               case '\t':
                                   fs += "\\t";
                                   break;
                               case '\b':
                                   fs += "\\b";
                                   break;
                               case '\r':
                                   fs += "\\r";
                                   break;
                               case '\f':
                                   fs += "\\f";
                                   break;
                               case '\"':
                                   fs += "\\\"";
                                   break;
                               case  '\\':
                                   fs += "\\" + "\\";
                                   break;
                               default:
                                   fs += c;
                           }

                       }
                       System.out.print(fs);
                   } else {
                       System.out.print(token.value);
                   }

                }
                System.out.println();
                token = getToken();

            }
            if (token.getId() < 0) {

                System.out.println("token = " + token.toString());
                System.out.println("Error");
                token = getToken();
            }
        }

    } */

    public void generate(StringBuilder builder) {
        builder.append("target datalayout = \"e-p:64:64:64-i1:8:8-i8:8:8-i16:16:16-i32:32:32-i64:64:64-f32:32:32-f64:64:64-v64:64:64-v128:128:128-a0:0:64-s0:64:64-f80:128:128-n8:16:32:64-S128\"\n" +
                "target triple = \"x86_64-apple-macosx10.7.0\"\n");
        ArrayList classes = Program.getClasses();
        for (int i = classes.size() -1; i >= 0 ;i--) {
            ClassNode node = (ClassNode) classes.get(i);
            node.generate(builder);
        }

        CodeGenerator.appendMain(builder);
        CodeGenerator.newLine(builder);
        builder.append("declare noalias i8* @_Znwm(i64)");
        CodeGenerator.newLine(builder);
        //To change body of created methods use File | Settings | File Templates.
    }
}
