/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 4/10/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */

import beaver.Parser;
import cool.MyCoolParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyCoolMain {
    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(args[0]);

            MyCoolParser parser = new MyCoolParser(inputStream);
            parser.parse2();

            System.out.println("Parse Successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Parser.Exception e) {
            System.out.println("Parse Error");
        }

    }
}
