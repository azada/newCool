package cool.exception;

import beaver.Symbol;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/21/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyExeption extends Exception {
    String errorMsg;
    Symbol errorInfo ;
    public MyExeption(String errorMsg, Symbol e){
        this.errorMsg = errorMsg;
        this.errorInfo = e;
    }
    public void pritnError(){
        System.out.println(errorMsg);
    }
}
