import javax.imageio.IIOException;
import java.io.*;

public class Parser {
    RandomAccessFile raf;
    String current_instruction="";
    long length;

    public Parser(String fileName) throws IOException {
        this.raf = new RandomAccessFile(fileName, "r");
        this.length = this.raf.length();
        this.current_instruction = "";
    }

    /**
     * checks if the given file has more kines to read
     * @return true is it has
     * @throws IOException
     */
    public boolean hasMoreLines() throws IOException {
        return (this.raf.getFilePointer() != length);
    }

    /**
     * advance the file to the next asm instruction (skip spaces)
     * set the current_instruction field to be that instruction.
     * if reaeach end of file set the current_instruction to null.
     *
     * @throws IOException
     */
    public void advance() throws IOException {
        this.current_instruction="";
        boolean read = true;
        int c = ' ';
        //skip comments and white spaces
        while (read) {
            c = raf.read();
            if (c == '/') {
                raf.readLine();
            } else if (c == -1 || !Character.isWhitespace(c)) {
                read = false;
            }
        }
        //set the current_instruction field.
        if (c != -1) {
            this.current_instruction = "" + (char) c;
            current_instruction += raf.readLine();
        }
    }

    /**
     * return the type of the current_command
     *
     * @return 0 for C_PUSH;  1 for C_POP ;2 for C_ARITHMETIC
     */
    public int commandType() {
        String commandType = getCommandType();
        if (commandType.equals("push")){
            return 0;
        }
        else if (commandType.equals("pop")){
            return 1;
        }
        else if (commandType.equals("add") || commandType.equals("sub") || commandType.equals("neg") || commandType.equals("eq")
        || commandType.equals("lt") || commandType.equals("gt") || commandType.equals("and") || commandType.equals("or")
        || commandType.equals("not")){
            return 2;
        }
        else if (commandType.equals("label")) {
            return 3;
        }
        else if (commandType.equals("goto")) {
            return 4;
        }
        else if (commandType.equals("if-goto")) {
            return 5;
        }
        else if (commandType.equals("function")) {
            return 6;
        }
        else if (commandType.equals("call")) {
            return 7;
        }
        else if (commandType.equals("return")) {
            return 8;
        }
        return -1;
    }

    /**
     * extract first argument from a given instruction
     * @return String which represents first argument of a given instruction
     */
    public String arg1(){
        String arg1 = "";

        //if the current command type is C_ARITHMETIC return the command itself.
        if(this.commandType() == 2) {
            String commandType = getCommandType();
            return commandType;
        }

        //otherwise (push/pop/label/goto/if-goto/function/call) we return the first argument.
        else {
            char c = this.current_instruction.charAt(0);
            int i = 0;

            //this loop skip the command type.
            while (c != ' ') {
                i++;
                c = this.current_instruction.charAt(i);
            }
            i++;
            c = this.current_instruction.charAt(i);
            i++;
            //this loop axtract our first argument.
            while (c !=' ' && c != '\n') {
                arg1 += c;
                if (i<this.current_instruction.length()){
                    c = this.current_instruction.charAt(i);
                    i++;
                }
                else {
                    break;
                }
            }
            c = this.current_instruction.charAt(i);
        }
        return arg1;
    }

    /**
     * extract second argument from a given instruction
     * @return integer which represents second argument of a given instruction
     */

    public int arg2(){
        String arg2str = "";
        int spaceCount = 0;
        char c = this.current_instruction.charAt(0);
        int i = 0;
        //this loop skip the command type and first argument.
        while (c !=' ' || spaceCount < 2 ) {
            i++;
            c = this.current_instruction.charAt(i);
            if (c == ' ') {
                spaceCount++;
            }
        }
        i++;
        c = this.current_instruction.charAt(i);
        i++;
        //this loop axtract our second argument.
        while (c !=' ' && c != '\n') {
            arg2str += c;
            if (i<this.current_instruction.length()){
                c = this.current_instruction.charAt(i);
                i++;
            }
            else {
                break;
            }
        }
        int arg2 = Integer.parseInt(arg2str);
        return arg2;
    }

    private String getCommandType() {
        String commandType = "";
        char c = this.current_instruction.charAt(0);
        int i = 1;
        while (c !=' ' && c != '\n') {
            commandType = commandType + c;
            if (i<this.current_instruction.length()){
                c = this.current_instruction.charAt(i);
                i++;
            }
            else {
                break;
            }
        }
        return commandType;
    }
}

