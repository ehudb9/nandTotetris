import java.util.HashMap;

/*
Deals only with C-instructions: dest = comp ; jump
*/

public class Code{
    HashMap <String , String> dest_table = new HashMap<>();
    HashMap <String , String> comp_table = new HashMap<>();
    HashMap <String , String> jump_table = new HashMap<>();
    //costructor: init the tables According to the language specification: 
    public Code(){
        // init the dest_table
        dest_table.put("null","000");
        dest_table.put("M","001");
        dest_table.put("D","010");
        dest_table.put("DM","011");
        dest_table.put("MD","011");
        dest_table.put("A","100");
        dest_table.put("AM","101");
        dest_table.put("AD","110");
        dest_table.put("ADM","111");

        // init the comp_table
        comp_table.put("0","0101010");
        comp_table.put("1","0111111");
        comp_table.put("-1","0111010");
        comp_table.put("D","0001100");
        comp_table.put("A","0110000");
        comp_table.put("M","1110000");
        comp_table.put("!D","0001101");
        comp_table.put("!A","0110001");
        comp_table.put("!M","1110001");
        comp_table.put("-D","0001111");
        comp_table.put("-A","0110011");
        comp_table.put("-M","1110011");
        comp_table.put("D+1","0011111");
        comp_table.put("A+1","0110111");
        comp_table.put("M+1","1110111");
        comp_table.put("D-1","0001110");
        comp_table.put("A-1","0110010");
        comp_table.put("M-1","1110010");
        comp_table.put("D+A","0000010");
        comp_table.put("D+M","1000010");
        comp_table.put("D-A","0010011");
        comp_table.put("D-M","1010011");
        comp_table.put("A-D","0000111");
        comp_table.put("M-D","1000111");
        comp_table.put("D&A","0000000");
        comp_table.put("D&M","1000000");
        comp_table.put("D|A","0010101");
        comp_table.put("D|M","1010101");

        //init the jump_table
        jump_table.put("null","000");
        jump_table.put("JGT","001");
        jump_table.put("JEQ","010");
        jump_table.put("JGE","011");
        jump_table.put("JLT","100");
        jump_table.put("JNE","101");
        jump_table.put("JLE","110");
        jump_table.put("JMP","111");
    }
    /**
     * dest(): Returns the binary representation of the parsed dest field
     * @param dest_ins
     * @return string represents 3 bits
     */
    public String dest(String dest_ins){
        return dest_table.get(dest_ins);
    }
    /**
     * comp(): Returns the binary representation of the parsed comp field
     * @param comp_ins
     * @return string represents 7 bits
     */
    public String comp(String comp_ins){
        return comp_table.get(comp_ins);
    }
    
    /**
     * dest(): Returns the binary representation of the parsed jump field
     * @param jump_ins
     * @return string represent3 bits 
     */
    public String jump(String jump_ins){
        return jump_table.get(jump_ins);
    }
}