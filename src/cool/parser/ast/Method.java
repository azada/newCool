package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolItem;
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
    ArrayList actuals;

    public Method(String id, ArrayList actuals) {
        super(id);
        this.actuals = actuals;

    }

    @Override
    public boolean check(SymbolNode pTable) throws MyExeption {
        // we should check the Type of primary and make sure it has a method with this method.
        boolean result = true;
        FeatureMethod temp ;
            // we check if this primary type has this method defined
        String tTemp;
        tTemp = pTable.lookup("THIS").getType();

        // first we check if we have this type defined
        if (Program.typeTableContains(tTemp)){
            // we check if this primary type has this method defined
            if (Program.getTableRow(tTemp) != null){
                if (!Program.getTableRow(tTemp).containsKey(name)){
                    // if this Id didn't have this method in itself, we should look up to find this method.
                    if (pTable.lookup("SUPER")!= null){
                        String superType = pTable.lookup("SUPER").getType();
                        temp = Program.fetchMethod(superType, name);
                        if(temp == null){
                            // this means that this method doesn't exsist
                            result = false;
                            Program.addError(new MyExeption("method '" + name + "' doesn't exist within " + tTemp, this));
                            return false;
                        }
                        else{
                            this.expType = temp.type;
                        }

                    }
                    else{
                        Program.addError(new MyExeption("method '" + name + "' doesn't exist within " + tTemp + " or it's supersss", this));
                        return false;
                    }
                }
                else{
                    //this method exists within this class
                    temp = Program.getTableRow(tTemp).get(name);
                    this.expType = temp.type;
                }
            }
            else{
                Program.addError(new MyExeption("method '" + name + "' doesn't exist within " + tTemp, this));
                return false;
            }

        }
        else{
            // if this primary type has not been defined throw an error
            Program.addError(new MyExeption("there is no such type '"+ tTemp + "' defined",this));
            System.out.println(name);
            return false;
        }
        // now we should check the actuals
        for (int i=0 ; i<actuals.size(); i++){
            boolean ac = ((Expr)this.actuals.get(i)).check(pTable);
            result = result && ac;
        }
        if (temp != null)  {
            // we should make sure we have the same number of actuals and formals in method call
            if (temp.formals.size() != actuals.size()){
                Program.addError(new MyExeption(temp.formals.size()+ " number of argument needed and " + actuals.size() + " are given",this));
                result = false;
            }
            //and make sure we have the same type in actuals as we had in feature methods.
            for (int i = 0 ; i< temp.formals.size() ; i++){
                if (!Program.isConsistant(((Formal) (temp.formals.get(i))).type, ((Expr) actuals.get(i)).expType)){
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
