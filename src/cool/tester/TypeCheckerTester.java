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
 * User: azada
 * Date: 5/22/13
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypeCheckerTester extends TestCase{


    public void testEverything() {
        System.out.println("TypeCheckerTester.testEverything");
        try {
            FileInputStream file = new FileInputStream("examples/gfx.cool");
            MyCoolParser parser = new MyCoolParser(file);
            parser.parse2();

            parser.checker();
            Program.printErrors();
            Program.clear();
            System.out.println("**************************************************************************************");

            Assert.assertTrue(true);

        } catch (FileNotFoundException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Parser.Exception e ) {
            System.out.println("e = " + e);
            Assert.assertTrue(false);
        }
    }
//    public void testPedram() {
//        System.out.println("TypeCheckerTester.testPedram");
//        try {
//            FileInputStream file = new FileInputStream("testcases/testPedram.cool");
//            MyCoolParser parser = new MyCoolParser(file);
//            parser.parse2();
//
//            parser.checker();
//            Program.printErrors();
//            Program.clear();
//            System.out.println("**************************************************************************************");
//
//            Assert.assertTrue(true);
//
//        }
//        catch (FileNotFoundException e) {
//            Assert.assertTrue(false);
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (Parser.Exception e ) {
//            System.out.println("error = " + e);
//
//            Assert.assertTrue(false);
//        }
//    }
}
