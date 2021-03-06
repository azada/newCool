package cool.parser.ast;

import cool.exception.MyException;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/19/13
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Match extends Expr {
    Expr primary;
    ArrayList cases;
    public Match(Expr primary, ArrayList cases) {
        this.primary = primary;
        this.cases = cases;
    }

    @Override
    public boolean check(SymbolNode pTable) throws MyException {
        boolean result = true;
        this.classType = pTable.type;

        /////////////////////////////////////////////////////////////////////////////////
        boolean ex = false;
        try {
            ex = primary.check(pTable);
        } catch (MyException myException) {
            myException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        /////////////////////////////////////////////////////////////////////////////////


        for (int i=0 ; i<cases.size(); i++){
            boolean vf = false;
            try {
                vf = ((Case)this.cases.get(i)).check(pTable);
            } catch (MyException myException) {
               throw myException;
            }
            result = result && vf;

        }
        if (cases.size() ==0 ){
            Program.addError(new MyException("Match needs at least one case",this));
            result = false;
        }
        if (cases.size()==1){
            this.expType = ((Case)(cases.get(0))).expType;
        }
        else{
            String temp = ((Case)(cases.get(0))).expType;
            for (int i=1; i< cases.size(); i++){
               temp = Program.mutualParent(temp,((Case)(cases.get(i))).expType);
            }
            //if we didn't have a mutual parent
            if (temp == null){
                Program.addError(new MyException("cases types did not have a mutual parent to be compared with the primary" ,this));
                result = false;
            }
            else{
                this.expType = temp;
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result && ex;
    }

    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
