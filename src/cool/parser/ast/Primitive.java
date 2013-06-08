package cool.parser.ast;

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

}
