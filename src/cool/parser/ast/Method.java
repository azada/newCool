package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 6/4/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Method extends Id {
    String id ;
    ArrayList actuals;

    public Method(String id, ArrayList actuals) {
        super(id);
        this.actuals = actuals;
    }

    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        // we should check the Type of primary and make sure it has an id with this method.
        boolean result = true;
        FeatureMethod temp = null;

            // we check if this primary type has this method defined
            if (!Program.getInstance().getTableRow("THIS").containsKey(id)){
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
                temp = Program.getInstance().getTableRow("THIS").get(id);
                this.expType = temp.type;
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
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void accept() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
