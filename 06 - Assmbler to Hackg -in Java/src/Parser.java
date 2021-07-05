import javax.imageio.IIOException;
import java.io.*;

public class Parser {
    // constans for type instruction:
    // A for @ - decimal num or a symbol
    public static int A_INSTRUCTION = 0;
    // C for : dest=comp;jump
    public static int C_INSTRUCTION = 1;
    // L for symbol
    public static int L_INSTRUCTION = 2;

    RandomAccessFile file;
    String current_instruction = "";
    long length;

    // constructor- opens file with a given fileName
    public Parser(String fileName) throws IOException {
        this.file = new RandomAccessFile(fileName, "r");
        this.length = this.file.length();
        this.current_instruction = "";
    }

    /**
     * checks if the given file has more lines to read
     * 
     * @return true is it has
     * @throws IOException
     */
    public boolean hasMoreLines() throws IOException {
        return (this.file.getFilePointer() != length);
    }

    /**
     * skips over whitespace and comments, if neccessary. reads the next instruction
     * from the input and make it the current instruction. will be called if
     * hasMoreLine is True
     * 
     * @throws IOExcetion
     */
    public void advance() throws IOException {
        this.current_instruction = "";
        boolean flag = true;
        int c = ' ';
        // skipping comments and white spaces
        while (flag) {
            c = file.read();
            if (c == '/') {
                file.readLine();
            } else if (!Character.isWhitespace(c) || c == -1) {
                flag = false;
            }
        }
        // set current_instruction field - copy the all line.
        if (c != -1) {
            this.current_instruction = "" + (char) c;
            this.current_instruction += file.readLine();
        }
    }

    /**
     * return the type of the instruction
     *
     * @return 0 for A_instruction; 1 for C_instruction ;2 for L_instruction
     */
    public int instructionType() {
        if (this.current_instruction != "") {
            char c = this.current_instruction.charAt(0);
            if (c == '@') {
                return A_INSTRUCTION;
            }
            if (c == '(') {
                return L_INSTRUCTION;
            }
            return C_INSTRUCTION;
        }
        return -1;
    }

    /**
     * parse symbol from A and L instructions
     *
     * @return string which represents the symbol
     */
    public String symbol() {
        String symbol = "";
        char c = this.current_instruction.charAt(0);
        int i = 1;
        if (c == '(') {
            while ((c = this.current_instruction.charAt(i)) != ')') {
                symbol += c;
                i++;
            }
        } else {
            while (i <= (this.current_instruction.length() - 1) && !Character.isWhitespace(c = this.current_instruction.charAt(i))) {
                symbol += c;
                i++;
            }
        }
        return symbol;
    }

    /**
     * extract the dest part from a given C_instruction: dest = comp ; jump
     * 
     * @return String which represents the dest part
     */
    public String dest() {

        int length = this.current_instruction.length();
        int eq_sign = -1;
        String dest = "null";
        int i = 0;
        while (i < length) {
            if (this.current_instruction.charAt(i) == '=') {
                eq_sign = i;
                break;
            }
            i++;
        }
        if (eq_sign == -1) {
            return dest;
        }
        dest = this.current_instruction.substring(0, eq_sign);
        return dest;
    }

    /**
     * extract the comp part from only a given C_instruction: dest = comp ; jump
     * 
     * @return String which represents the comp part
     */
    public String comp() {
        int length = this.current_instruction.length();
        int eq_sign = 0;
        int semiColon_sign = length;
        int i = 0;
        while (i < length) {
            if (this.current_instruction.charAt(i) == '=') {
                eq_sign = i + 1;
            }
            if (this.current_instruction.charAt(i) == ';' || Character.isWhitespace(this.current_instruction.charAt(i))
                    || this.current_instruction.charAt(i) == '/') {
                semiColon_sign = i;
                break;

            }
            i++;
        }
        return this.current_instruction.substring(eq_sign, semiColon_sign);
    }

    /**
     * extract the jump part from a given C_instruction: dest = comp ; jump
     * 
     * @return String which represents the jump part
     */
    public String jump() {
        int semicolon_sign = -1;
        int length = this.current_instruction.length();
        int i = 0;
        String jump = "null";
        while (i < length) {
            if (this.current_instruction.charAt(i) == ';') {
                semicolon_sign = i;
            }
            if (Character.isWhitespace(this.current_instruction.charAt(i))
                    || this.current_instruction.charAt(i) == '/') {
                length = i;
                break;
            }
            i++;
        }
        if (semicolon_sign == -1) {
            return jump;
        }
        jump = this.current_instruction.substring(semicolon_sign + 1, length);
        return jump;
    }

}