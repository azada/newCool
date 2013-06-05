package cool.parser.ast;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/7/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONLogger {

    public static void logList() {

    }

    public static void openNode(String name) {
        System.out.print("\""+name+"\"" + ":" + "{ ");
    }

    public static void closeNode() {
        System.out.print(" }");
    }


    public static void attribute(String name, String value) {
        System.out.print("\""+name+"\"" + " : " + "\""+value+ "\"");
    }

    public static void nextAttribute() {
        System.out.print(",");
    }
    public static void openListAttribute(String name) {
        System.out.print("\""+name +"\""+ " : [ ");

    }



    public static void closeListAttribute() {
        System.out.print("] ");
    }

    public static void openBrace() {
        System.out.print("{");
    }

    public static void closeBrace() {
        System.out.print("}");
    }
}
