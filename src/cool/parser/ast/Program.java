package cool.parser.ast;

import cool.exception.MyExeption;
import cool.symbol.SymbolNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/22/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
// a singleton for storing global information about the program
public class Program {

    private static ArrayList classes = new ArrayList();
    ///////////////////////type /////////// function name, it's object
    private static HashMap<String,HashMap<String,FeatureMethod> > typeTable = new HashMap<String,HashMap<String,FeatureMethod> >() ;
    // in order to store the class's object as a pair to the classe's name, we need another hashmap.
    public static HashMap<String , ClassNode> typeClassTable = new HashMap<String, ClassNode>();
    public static SymbolNode programSymbolNode = new SymbolNode();
    private static ArrayList<MyExeption> errorList = new ArrayList<MyExeption>();
    public static HashMap<String, String> inheritance = new HashMap<String, String>();
    private static Program instance = new Program();

    private Program() {
    }
    public static Program getInstance(){
        if(instance.typeTable.isEmpty()){
            instance.typeTable.put("Int", null);
            instance.typeTable.put("String", null);
            instance.typeTable.put("Boolean", null);
        }
        return instance;
    }
    public static boolean isConsistant(String c, String p){
        if (c.equals(Expr.NULL_TYPE)){
            return true;
        }
        if(instance.inheritance.containsKey(c)){
            if (c.equals(p)){
                System.out.println("coooooOOOOOOoooool");
                return true;
            }
            String r = instance.inheritance.get(c);
            if (r == null)
                return false;
            if (r.equals(p))
                return true;
            else{
                return isConsistant(r, p);
            }
        }
        else
            return false;
    }
    public static String getSuper(String child){
        if(instance.inheritance.containsKey(child)){
            return instance.inheritance.get(child);
        }
        else{
            return null;
        }
    }
    public static String mutualParent(String a, String b){
        ArrayList<String> aList = new ArrayList<String>();
        ArrayList<String> bList = new ArrayList<String>();
        aList.add(a);
        bList.add(b);
        String temp = getInstance().inheritance.get(a);
        while(true){
            if(temp != null){
                if(temp != null){
                    aList.add(temp);
                    temp = getInstance().inheritance.get(temp);
                }
                else
                    break;
            }
            else
                break;
        }
        temp = getInstance().inheritance.get(b);
        while(true){
            if(temp != null){
                aList.add(temp);
                temp = getInstance().inheritance.get(temp);
            }
            else
                break;
        }
        //now we have both inheritance trees in two lists
        Collections.reverse(aList);
        Collections.reverse(bList);
        String result = null;
        for(int i = 0 ; i< Math.min(aList.size(), bList.size()) ; i++){
            if (aList.get(i).equals(bList.get(i)))
                result = aList.get(i);
            else
                break;
        }
        return result;


    }
    // we need a method to look up a "method" starting from the base class up untill the final class
    public static FeatureMethod fetchMethod(String superType,String method){
            if(superType == null){
                return null;
            }
            // if the class exists,
            HashMap<String, FeatureMethod> hashTemp = getTableRow(superType);
            if(hashTemp != null){
                // then we check if it has this method
                FeatureMethod tempMethod = hashTemp.get(method);
                if(tempMethod!= null){
                    //it means that this method exists there
                    return tempMethod;
                }
                else{
                    //it means that this method doesn't exist there
                    // then we should get this and go up until we find it.
                    String tempSuper = getSuper(superType);
                    return fetchMethod(superType, method);
                }
            }
            else{
                String tempSuper = getSuper(superType);
                return fetchMethod(superType, method);
            }

    }
    public static ArrayList getClasses(){
        return classes;
    }
    public static void setClasses(ArrayList cls){
        classes = cls;
    }
    public static boolean typeTableContains(String a){
        return instance.typeTable.containsKey(a);
    }
    public static HashMap<String,FeatureMethod>  getTableRow(String a){
        return instance.typeTable.get(a);
    }
    public static void typeTablePut(String a , HashMap<String,FeatureMethod> b){
        instance.typeTable.put(a, b);
    }
    public static SymbolNode getSymbolNode(){
        return programSymbolNode;
    }
    public static void addError(MyExeption error){
        errorList.add(error);
    }
    public static void printErrors(){
        if (errorList.size() == 0){
            System.out.println("TYPE CHECKING HAS BEEN DONE SUCCESSFULLY");
            return;
        }
        for (int i = 0 ; i< errorList.size() ;i++){
            errorList.get(i).pritnError();
        }
    }
    public static void clear(){
        classes.clear();
        typeTable.clear();
        errorList.clear();
    }

}
