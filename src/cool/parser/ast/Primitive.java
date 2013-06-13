package cool.parser.ast;

import cool.codegen.CodeGenerator;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/8/13
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Primitive extends ClassNode{
    public Primitive(String type, ArrayList varFormals, Extends ext, ArrayList featureList) {
        super(type, varFormals, ext, featureList);
    }

    public Primitive() {
    }


    public void generateReference(StringBuilder builder) {
        if (this.type.equals("Int")) {
            CodeGenerator.appendInt(builder);
        }

    }

    public void generateInstance(StringBuilder builder) {
        if (this.type.equals("Int")) {
            CodeGenerator.appendInt(builder);
        }
    }

    public int getSize() {
        if (this.type.equals("Int")) {
            return 4;
        }
        return 0;
    }
}
