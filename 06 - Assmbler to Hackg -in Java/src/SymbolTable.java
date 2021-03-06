import java.util.HashMap;

public class SymbolTable {
    
    HashMap<String,Integer> table;
    //initilalize the basic table.
    public SymbolTable(){
        this.table=new HashMap <>();
        table.put("R0",0);
        table.put("R1",1);
        table.put("R2",2);
        table.put("R3",3);
        table.put("R4",4);
        table.put("R5",5);
        table.put("R6",6);
        table.put("R7",7);
        table.put("R8",8);
        table.put("R9",9);
        table.put("R10",10);
        table.put("R11",11);
        table.put("R12",12);
        table.put("R13",13);
        table.put("R14",14);
        table.put("R15",15);
        table.put("SCREEN",16384);
        table.put("KBD",24576);
        table.put("SP",0);
        table.put("LCL",1);
        table.put("ARG",2);
        table.put("THIS",3);
        table.put("THAT",4);
    }
    //Adds <symbol, address> to the table
    public void addEntry(String symbol,int address){
        this.table.put(symbol,address);
    }
    //contains(symbol): Checks if symbol exists in the table
    public boolean contains(String symbol) {
        return this.table.containsKey(symbol);
    }
    //getAddress(symbol): Returns the address associated with symbol
    public int getAddress(String symbol) {
        return table.get(symbol);
    }
}



