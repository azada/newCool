package cool.exception;

import beaver.Symbol;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/4/13
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypeException extends MyExeption {
    public TypeException(String errorMsg, Symbol e) {
        super(errorMsg, e);
    }
}
