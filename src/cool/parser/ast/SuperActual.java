package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/19/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
//SUPER DOT ID.id actuals.a

public class SuperActual extends Expr {
    ArrayList actuals;
    String id ;
    public SuperActual(String id,ArrayList actuals) {
        this.id = id;
        this.actuals = actuals;
    }

    @Override
    public boolean check(SymbolNode pTable) {
        boolean result = true;
        String superType = pTable.lookup("SUPER").getType();
        if(superType == null){
            Program.addError(new MyExeption("this class doesn't have a super class",this));
            result = false;
            return result;
        }
        FeatureMethod temp = Program.fetchMethod(superType,id);
        if(temp == null){
            Program.addError(new MyExeption("there is no such method in this type's supper classes ",this));
            return result;
        }
        if (temp.formals.size() != actuals.size()){
            Program.addError(new MyExeption(temp.formals.size()+ " number of argument needed and " + actuals + " are given",this));
            result = false;
        }
        for (int i = 0 ; i< temp.formals.size() ; i++){
            if (!Program.getInstance().isConsistant(((Formal) (temp.formals.get(i))).type, ((Expr) actuals.get(i)).expType)){
                Program.addError(new MyExeption("type of actuals doesn't match argument list defined in the method "+ id,this));
                result = false;
                break;
            }
        }

        //To change body of implemented methods use File | Settings | File Templates.
        return result;
    }
    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
