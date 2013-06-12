package cool.exception;

import beaver.Symbol;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/9/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FatalErrorException extends MyException {
    public FatalErrorException(String errorMsg, Symbol e) {
        super(errorMsg, e);
    }
}
