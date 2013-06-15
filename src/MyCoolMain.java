/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 4/10/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */

import beaver.Parser;
import cool.MyCoolParser;
import cool.parser.ast.Program;

import java.io.*;

public class MyCoolMain {
    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(args[0]);

            MyCoolParser parser = new MyCoolParser(inputStream);
            parser.parse2();
            System.out.println("Parse Successful");
            parser.checker();
            parser.calculateSize();
            Program.printErrors();
            System.out.println("**************************************************************************************");

            StringBuilder builder = new StringBuilder();
            parser.generate(builder);
            String llvmCode = builder.toString();
            BufferedWriter writer = null;
            writer = new BufferedWriter( new FileWriter(args[1]));
            writer.write(llvmCode);
            writer.close( );

//            System.out.println(llvmCode);


            Program.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Parser.Exception e) {
            System.out.println("Parse Error");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
