import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * The VMWriter is a simple module that writes individual VM commands to the
 * output .vm file.
 */
public class VMWriter {
    RandomAccessFile out;
    
    public static final int CONST=27;
    public static final int ARG=28;
    public static final int LOCAL=29;
    public static final int STATIC=15;
    public static final int THIS=26;
    public static final int THAT=32;
    public static final int POINTER=33;
    public static final int TEMP=34;
    public static final int ADD=35;
    public static final int SUB=36;
    public static final int NEG=37;
    public static final int EQ=38;
    public static final int GT=39;
    public static final int LT=40;
    public static final int AND=41;
    public static final int OR=42;
    public static final int NOT=43;

    /**
     * creates a new output /vm file, and prepares it for writing
     * 
     * @param out
     * @throws IOException
     */
    public VMWriter (RandomAccessFile out) throws IOException {   
        this.out = out;
    }

    /**
     * returns the segment type in String
     * 
     * @param segment
     * @return String seg = segment type
     */
    public String get_seg_type (int segment) {    
        String seg = "";
        if (segment == CONST) seg = "constant ";
        if (segment == ARG) seg = "argument ";
        if (segment == LOCAL) seg = "local ";
        if (segment == STATIC) seg = "static ";
        if (segment == THIS) seg = "this ";
        if (segment == THAT) seg = "that ";
        if (segment == POINTER) seg = "pointer ";
        if (segment == TEMP) seg = "temp ";
        return seg;
    }

    /**
     * returns the command type in String
     * 
     * @param command
     * @return String com = command type
     */
    public String get_command_type (int command) {    
        String com = "";
        if (command == ADD) com = "add ";
        if (command == SUB) com = "sub ";
        if (command == NEG) com = "neg ";
        if (command == EQ) com = "eq ";
        if (command == GT) com = "gt ";
        if (command == LT) com = "lt ";
        if (command == AND) com = "and ";
        if (command == OR) com = "or ";
        if (command == NOT) com = "not ";
        return com;
    }

    /**
     * writes the given VM push command
     * 
     * @param segment
     * @param index
     * @throws IOException
     */
    public void writePush(int segment, int index) throws IOException{   
        String seg = get_seg_type(segment);
        out.writeBytes("push " + seg + Integer.toString(index) +"\n");
    }

    /**
     * writes the given VM pop command
     * 
     * @param segment
     * @param index
     * @throws IOException
     */
    public void writePop(int segment, int index) throws IOException{     
        String seg = get_seg_type(segment);
        out.writeBytes("pop " + seg + Integer.toString(index) +"\n");
    }

    /**
     * writes the given VM aritmentic-logical command
     * 
     * @param command
     * @throws IOException
     */
    public void writeArithmetic(int command) throws IOException{   
        String com = get_command_type(command);
        out.writeBytes( com +"\n");
    }

    /**
     * writes the given VM lable command
     * 
     * @param label
     * @throws IOException
     */
    public void writeLabel(String label) throws IOException{      
        out.writeBytes("label " + label + "\n");
    }

    /**
     * // writes the given VM goto command
     * 
     * @param label
     * @throws IOException
     */
    public void writeGoto(String label) throws IOException{      
        out.writeBytes("goto " + label + "\n");

    }

    /**
     * writes the given VM if-goto command
     * 
     * @param label
     * @throws IOException
     */
    public void writeIf(String label) throws IOException{       
        out.writeBytes("if-goto " + label + "\n");
    }

    /**
     * writes the given VM call command
     * 
     * @param name
     * @param nArgs
     * @throws IOException
     */
    public void writeCall(String name, int nArgs) throws IOException{     
        out.writeBytes("call " + name + " " + Integer.toString(nArgs) + "\n");
    }

    /**
     * writes the given VM function command
     * 
     * @param name
     * @param nLocals
     * @throws IOException
     */
    public void writeFunction(String name, int nLocals) throws IOException{     
        out.writeBytes("function " + name + " " + Integer.toString(nLocals) + "\n");
    }

    /**
     * writes the given VM return command
     * 
     * @throws IOException
     */
    public void writeReturn() throws IOException{     
        out.writeBytes("return\n");
    }

    /**
     * closes the output file
     * 
     * @throws IOException
     */
    public void close() throws IOException{     
        out.close();
    }

}