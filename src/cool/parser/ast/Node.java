package cool.parser.ast;

/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 5/4/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */

import beaver.Symbol;
import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

public abstract class Node extends Symbol
{
    public abstract void accept();
    public abstract boolean check(SymbolNode pTable) throws MyExeption;
}


