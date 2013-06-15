package cool.symbol;


/**
 * Created with IntelliJ IDEA.
 * User: azada
 * Date: 5/21/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SymbolItem {
    String id;
    String type;
    String inClass;
    int address;
    boolean init;

    public void setClass(String aClass) {
        inClass = aClass;
    }

    public String getInClass() {
        return inClass;
    }

    public SymbolItem(String id, String type,int address, boolean init) {
        this.id = id;
        this.type = type;
        this.init = init;
        this.address = address;
    }
    public String getType(){
        return type;
    }
}
