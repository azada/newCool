package cool.parser.ast;

import cool.exception.MyException;
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
    private static HashMap<String , ClassNode> typeClassTable = new HashMap<String, ClassNode>();
    private static SymbolNode programSymbolNode = new SymbolNode();
    private static ArrayList<MyException> errorList = new ArrayList<MyException>();
    private static HashMap<String, String> inheritance = new HashMap<String, String>();
    private static Program instance = new Program();

    private Program() {
//        typeTable.put("Int", null);
//        typeTable.put("String", null);
//        typeTable.put("Boolean", null);
//        inheritance.put("Int", "Any");
//        inheritance.put("String", "Any");
//        inheritance.put("Boolean", "Any");
        inheritance.put("Any",null);
//        typeClassTable.put("Int",new Primitive("Int",null,null,null));
//        typeClassTable.put("String",new Primitive("String",null,null,null));
//        typeClassTable.put("Boolean",new Primitive("Boolean",null,null,null));

    }

    public static void putInheritance(String child, String parent){
        if (child.equals(parent)){
            return;
        }
        instance.inheritance.put(child,parent);
    }
    public static void putClassTable(String type1, ClassNode node){
        instance.typeClassTable.put(type1,node);

    }
    public static boolean classTableContains(String type){
        if (instance.typeClassTable.containsKey(type))
            return true;
        else
            return false;
    }
    public static ClassNode getClassNode(String type){
        return instance.typeClassTable.get(type);
    }

    public ClassNode fetchOriginalMethod(String type, String method){
        String superType = Program.getSuper(type);
        if (superType == null)
            return null;
        FeatureMethod temp = fetchMethod(superType,method);
        if (temp == null)
            return null;
        if (temp instanceof OverrideFeatureMethod)
            return fetchOriginalMethod(temp.classType, method);
        else if (temp  instanceof FeatureMethod)
            return getClassNode(temp.classType);
        else
            return null;

    }

    public static boolean isConsistent(String c, String p){
        if (c.equals("Nothing")){
            return true;
        }
        if (c == null){
            return false;
        }
        if (c.equals(Expr.NULL_TYPE)){
            return true;
        }
        if(instance.inheritance.containsKey(c)){
            if (c.equals(p)){
                return true;
            }
            String r = instance.inheritance.get(c);
            if (r == null)
                return false;
            if (r.equals(p))
                return true;
            else{
                return isConsistent(r, p);
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
        if(a.equals("Nothing")){
            return b;
        }
        if (b.equals("Nothing")){
            return a;
        }
        if(a.equals(b)){
            return a;
        }
        ArrayList<String> aList = new ArrayList<String>();
        ArrayList<String> bList = new ArrayList<String>();
        aList.add(a);
        bList.add(b);
        String temp = instance.inheritance.get(a);
        while(true){
            if(temp != null){
                if(temp != null){
                    aList.add(temp);
                    temp = instance.inheritance.get(temp);
                }
                else
                    break;
            }
            else
                break;
        }
        temp = instance.inheritance.get(b);
        while(true){
            if(temp != null){
                aList.add(temp);
                temp = instance.inheritance.get(temp);
            }
            else
                break;
        }
        //now we have both inheritance trees in two lists
        Collections.reverse(aList);
        Collections.reverse(bList);
        String result = "Any";
        for(int i = 0 ; i< Math.min(aList.size(), bList.size()) ; i++){
            if (aList.get(i).equals(bList.get(i)))
                result = aList.get(i);
            else
                break;
        }
        return result;
    }

    public static FeatureMethod lookupMethod(String instanceType,String method){
        HashMap methods = typeTable.get(instanceType);
        if (methods.containsKey(method)) {
            return (FeatureMethod) methods.get(method);
        } else {

            String superType = Program.getSuper(instanceType);
            FeatureMethod tempFeature = Program.fetchMethod(superType, method);
            return  tempFeature;
        }
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
                    return fetchMethod(tempSuper, method);
                }
            }
            else{
                String tempSuper = getSuper(superType);
                return fetchMethod(tempSuper, method);
            }

    }
    public static ArrayList getClasses(){
        return instance.classes;
    }
    public static void setClasses(ArrayList cls){
        instance.classes = cls;
    }
    public static boolean typeTableContains(String a){
        return instance.typeTable.containsKey(a);
    }
    public static HashMap<String,FeatureMethod> getTableRow(String a){
        return instance.typeTable.get(a);
    }
    public static void putTypeTable(String a, HashMap<String, FeatureMethod> b){
        instance.typeTable.put(a, b);
    }
    public static SymbolNode getSymbolNode(){
        return instance.programSymbolNode;
    }
    public static void addError(MyException error){
        instance.errorList.add(error);
    }
    public static void printErrors(){
        if (instance.errorList.size() == 0){
            System.out.println("TYPE CHECKING HAS BEEN DONE SUCCESSFULLY");
            return;
        }
        for (int i = 0 ; i< instance.errorList.size() ;i++){
            instance.errorList.get(i).pritnError();
        }
    }
    public static void clear(){
        instance.classes.clear();
        instance.typeTable.clear();
        instance.typeClassTable.clear();
        instance.inheritance.clear();
        instance.errorList.clear();
//        typeTable.put("Int", null);
//        typeTable.put("String", null);
//        typeTable.put("Boolean", null);
//        inheritance.put("Int", "Any");
//        inheritance.put("String", "Any");
//        inheritance.put("Boolean", "Any");
        inheritance.put("Any",null);
//        typeClassTable.put("Int",new Primitive("Int",null,null,null));
//        typeClassTable.put("String",new Primitive("String",null,null,null));
//        typeClassTable.put("Boolean",new Primitive("Boolean",null,null,null));
    }
//   public static Program getInstance(){
//        if(instance.typeTable.isEmpty()){
//            instance.typeTable.put("Int", null);
//            instance.typeTable.put("String", null);
//            instance.typeTable.put("Boolean", null);
//        }
//        if(instance.inheritance.isEmpty()){
//            instance.inheritance.put("Int", "Any");
//            instance.inheritance.put("String", "Any");
//            instance.inheritance.put("Boolean", "Any");
//        }
//        return instance;
//    }
}
