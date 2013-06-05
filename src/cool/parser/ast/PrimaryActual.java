package cool.parser.ast;

import cool.exception.MethodException;
import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/19/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryActual extends Expr {
    ArrayList actuals;
    Primary primary ;
    String id ;

    public PrimaryActual(ArrayList actuals, Primary primary, String id) {
        this.actuals = actuals;
        this.primary = primary;
        this.id = id;
    }
    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        // we should check the Type of primary and make sure it has an id with this method.
        boolean result = true;
        FeatureMethod temp = null;
        // first we check if we have this type defined
        if (Program.getInstance().getTableRow(primary.expType)!= null){
            // we check if this primary type has this method defined
            if (!Program.getInstance().getTableRow(primary.expType).containsKey(id)){
                // if this Id didn't have this method in itself, we should look up to find this method.
                String superType = pTable.lookup("SUPER").getType();
                temp = Program.fetchMethod(superType,id);
                if(temp == null){
                    // this means that this method doesn't exsist
                    result = false;
                    throw new MyExeption("this method doesn't exist", this);

                }
                this.expType = temp.type;

            }
            else{
                //this method exists within this class
                temp = Program.getInstance().getTableRow(primary.expType).get(id);
                this.expType = temp.type;
            }

        }
        else{
            // if this primary type has not been defined throw an error
            Program.addError(new MyExeption("there is no such type "+ primary.expType + " defined",this));
            result = false;
            throw new MethodException("this primary doesn't exist", this);
        }

        // now we should check the actuals
        for (int i=0 ; i<actuals.size(); i++){
            boolean ac = ((Expr)this.actuals.get(i)).check(pTable);
            result = result && ac;
        }
        if (temp != null)  {
        // we should make sure we have the same number of actuals and formals in method call
            if (temp.formals.size() != actuals.size()){
                Program.addError(new MyExeption(temp.formals.size()+ " number of argument needed and " + actuals + " are given",this));
                result = false;
            }
            //and make sure we have the same type in actuals as we had in feature methods.
            for (int i = 0 ; i< temp.formals.size() ; i++){
                if (!Program.getInstance().isConsistant(((Formal) (temp.formals.get(i))).type, ((Expr) actuals.get(i)).expType)){
                    Program.addError(new MyExeption("type of actuals doesn't match argument list defined in the method",this));
                    result = false;
                }
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
