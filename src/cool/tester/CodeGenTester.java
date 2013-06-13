package cool.tester;

import beaver.Parser;
import cool.MyCoolParser;
import cool.parser.ast.Program;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 6/5/13
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeGenTester extends TestCase {

    public void testFeatureMethod() {
        System.out.println("CodeGenTester.testFeatureMethod");
        try {
            FileInputStream file = new FileInputStream("testcases/testClassGen.cool");
            MyCoolParser parser = new MyCoolParser(file);
            parser.parse2();

            parser.checker();
            parser.calculateSize();
            Program.printErrors();
            System.out.println("**************************************************************************************");

            StringBuilder builder = new StringBuilder();
            parser.generate(builder);
            String llvmCode = builder.toString();
            System.out.println(llvmCode);


            Program.clear();


            Assert.assertTrue(true);

        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Parser.Exception e ) {
            System.out.println("e = " + e);
            Assert.assertTrue(false);
        }
    }




}
