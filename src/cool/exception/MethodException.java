package cool.exception;

import beaver.Symbol;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/4/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MethodException extends MyExeption {
    public MethodException(String errorMsg, Symbol e) {
        super(errorMsg, e);
    }
}
