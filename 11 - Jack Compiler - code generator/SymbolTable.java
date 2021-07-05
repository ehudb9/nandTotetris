import java.util.Hashtable;
/**
 * When compiling a class, we can build one class-level symbol table and one subroutine-level
//  symbol table, and reset the latter each time we start compiling a new subroutine
// • Both tables can be implemented as two separate instances of the same class, say SymbolTable
// • Each variable is assigned a running index within its scope (table) and kind. The index starts at 0,
//     increments by 1 after each time a new symbol is added to the table, and is reset to 0 when starting
//      a new scope (table)
// • When compiling an error-free Jack code, each symbol not found in the symbol tables can be 
//   assumed to be either a subroutine name or a class name
 */
public class SymbolTable {
    Hashtable<String, String> class_lavel_type;
    Hashtable<String, String> class_lavel_kind;
    Hashtable<String, Integer> class_lavel_count;
    Hashtable<String, String> sub_lavel_type;
    Hashtable<String, String> sub_lavel_kind;
    Hashtable<String, Integer> sub_lavel_count;
    int STATIC;
    int FIELD;
    int ARG;
    int VAR;

    /**
     * creates a new symbol table
     */
    public SymbolTable(){
        this.class_lavel_count=new Hashtable <>();
        this.class_lavel_type = new Hashtable <>();
        this.class_lavel_kind = new Hashtable <>();
        this.sub_lavel_count = new Hashtable <>();
        this.sub_lavel_type = new Hashtable <>();
        this.sub_lavel_kind = new Hashtable <>();
        this.STATIC = -1;
        this.FIELD = -1;
        this.ARG = -1;
        this.VAR = -1;
    }

    /**
     * empties the symbol table, and resets the four indexes to 0.
     * Should be called when starting to compile a subroutine declaration.
     */
    public void reset(){
        ARG=-1;
        VAR=-1;
        sub_lavel_kind.clear();
        sub_lavel_type.clear();
        sub_lavel_count.clear();
    }

    /**
     * defines (adds to the table) a new variable of the given name. type. and kind.
     * Assigns to it the index value of that kind, and adds 1 to the index
     *
     * @param String name
     * @param String type
     * @param String kind
     */
    public void define(String name,String type, String kind){
        if(kind.equals("STATIC")||kind.equals("FIELD")){
            if(!class_lavel_kind.containsKey(name)) {
                class_lavel_type.put(name, type);
                class_lavel_kind.put(name, kind);
                int varCount = (kind.equals("STATIC")) ? (STATIC = STATIC + 1) : (FIELD = FIELD + 1);
                class_lavel_count.put(name, varCount);
            }
        }else {
            if(!sub_lavel_kind.contains(name)) {
                sub_lavel_type.put(name, type);
                sub_lavel_kind.put(name, kind);
                int varCount = (kind.equals("ARG")) ? (ARG = ARG + 1) : (VAR = VAR + 1);
                sub_lavel_count.put(name, varCount);
            }
        }
    }

    /**
     * returns the number of variables of the given kind already defined in the table
     *
     * @param String kind
     * @return int var_num
     */
    public int varCount(String kind){
        int var_num=0;
        switch (kind) {
            case "STATIC":
                var_num= STATIC+1;
                break;
            case "FIELD":
                var_num=FIELD+1;
                break;
            case "ARG":
                var_num= ARG+1;
                break;
            case "LOCAL":
                var_num= VAR+1;
                break;
        }
        return var_num;
    }

    /**
     * returns the kind of the named identifier.
     * If the identifier is not found, returns NONE.
     *
     * @param String name
     * @return String kindOfName
     */
    public String KindOf(String name){
        if(sub_lavel_kind.containsKey(name)){
            return sub_lavel_kind.get(name);
        }if (class_lavel_kind.containsKey(name)){
            return class_lavel_kind.get(name);
        }
        return "NONE";
    }

    /**
     * returns the type of the named variable.
     *
     * @param Stirng name
     * @return String typeOfName
     */
    public String TypeOf(String name){
        if(sub_lavel_type.containsKey(name)){
            return sub_lavel_type.get(name);
        }if(class_lavel_type.containsKey(name)){
            return class_lavel_type.get(name);
        }
        return null;
    }

    /**
     * returns the index of the named variable.
     *
     * @param name
     * @return int indexOf
     */
    public int IndexOf(String name){
        if (sub_lavel_count.containsKey(name)){
            return sub_lavel_count.get(name);
        }if(class_lavel_count.containsKey(name)){
            return class_lavel_count.get(name);
        }
        return -1;
    }
}