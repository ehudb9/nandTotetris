import java.io.*;

public class CodeWriter{
    public static int C_PUSH = 0;
    public static int C_POP = 1;
    RandomAccessFile file;
    String current_command = "";
    long length;
    String fileName = "";
    public static int label = 0;
    
    public CodeWriter(String filename) throws IOException{
        String input = filename.replace(".vm", ".asm");
        this.fileName = extractFileName(filename);
        //opens file with in the same path , ends with ".asm"
        this.file = new RandomAccessFile("" + input, "rw");
        
    }  
    
    /**
     * extract the name from the arg input
     * @param input : string of the file
     * @return outputFile : string
     */
    public static String extractFileName(String input) {
        //extract the name from the arg input
        int i = input.length() - 1;
        String outputFile = "";
        char c = ' ';
        while (i >= 0 ) {
            c = input.charAt(i);
            if (c == '/' || c == '\\') {
                break;
            }
            outputFile = c + outputFile;
            i--;
        }
        int dot = -1;
        i = 0;
        while (i < outputFile.length()) {
            if (outputFile.charAt(i) == '.') {
                dot = i;
                break;
            }
            i++;
        }
        outputFile = "" + outputFile.substring(0, dot) + ".asm";
        return outputFile;
    }
    
    public void writeArithmetic(String command) throws IOException {
        if (command.equals("add")) {
            
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//*SP=*SP+D\n"+
            "A=M\n"+
            "D=M+D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
        if (command.equals("sub")) {
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//*SP=*SP-D\n"+
            "A=M\n"+
            "D=M-D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
        if (command.equals("neg")) {
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//*SP=!D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=-D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
        if (command.equals("eq")) {
            //SP--
            this.writeArithmetic("sub");
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//set *sp to 1\n"+
            "@SP\n"+
            "A=M\n"+
            "M=-1\n"+
            "//check if D==0, jump to the lable if it does\n"+
            "@lt."+label+"\n"+
            "D;JEQ\n"+
            "//D!=0-> *SP=false\n"+
            "@SP\n"+
            "A=M\n"+
            "M=0\n"+
            "(lt."+label+")\n");
            label++;
            file.writeBytes("//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
        }
        
        if (command.equals("gt")) {
            this.writeArithmetic("sub");
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//set *sp to 1\n"+
            "@SP\n"+
            "A=M\n"+
            "M=-1\n"+
            "//check if D>0, jump to the lable if it does\n"+
            "@gt."+label+"\n"+
            "D;JGT\n"+
            "//D!=0-> *SP=false\n"+
            "@SP\n"+
            "A=M\n"+
            "M=0\n"+
            "(gt."+label+")\n");
            label++;
            file.writeBytes("//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
        }
        if (command.equals("lt")) {
            this.writeArithmetic("sub");
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//set *sp to 1\n"+
            "@SP\n"+
            "A=M\n"+
            "M=-1\n"+
            "//check if D<0, jump to the lable if it does\n"+
            "@lt."+label+"\n"+
            "D;JLT\n"+
            "//D!=0-> *SP=false\n"+
            "@SP\n"+
            "A=M\n"+
            "M=0\n"+
            "(lt."+label+")\n");
            label++;
            file.writeBytes("//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
        }
        if (command.equals("and")) {
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//*SP=*SP&D\n"+
            "A=M\n"+
            "D=M&D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
        if (command.equals("or")) {
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=M\n"+
            "//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//*SP=*SP|D\n"+
            "A=M\n"+
            "D=M|D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
        if (command.equals("not")) {
            file.writeBytes("//SP--\n"+
            "@SP\n"+
            "M=M-1\n"+
            "//D=*SP\n"+
            "A=M\n"+
            "D=!M\n"+
            "//*SP=!D\n"+
            "@SP\n"+
            "A=M\n"+
            "M=D\n"+
            "//SP++\n"+
            "@SP\n"+
            "M=M+1\n");
            return;
        }
    }

    public void writePushPop ( int command, String segment,int index) throws IOException{
        if (command == C_PUSH) {
            if (segment.equals("local")) {
                file.writeBytes("//addr = LCL + i\n"+
                "@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "@LCL\n"+
                "D=M+D\n"+
                "//D = *addr\n"+
                "A=D\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if (segment.equals("argument")) {
                file.writeBytes("//addr = ARG + i\n"+
                "@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "@ARG\n"+
                "D=M+D\n"+
                "//D = *addr\n"+
                "A=D\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if (segment.equals("this")) {
                file.writeBytes("//addr = THIS + i\n"+
                "@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "@THIS\n"+
                "D=M+D\n"+
                "//D = *addr\n"+
                "A=D\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if (segment.equals("that")) {
                file.writeBytes("//addr = THAT + i\n"+
                "@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "@THAT\n"+
                "D=M+D\n"+
                "//D = *addr\n"+
                "A=D\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if (segment.equals("constant")) {
                file.writeBytes("@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "//D=constant\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if (segment.equals("static")) {
                file.writeBytes("//addr = filename.i\n"+
                "@" +fileName+"."+Integer.toString(index)+ "\n"+
                "//D = *addr\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
                
            }
            if (segment.equals("temp")) {
                file.writeBytes("//addr = 5 + i\n"+
                "@" + Integer.toString(index) + "\n"+
                "D=A\n"+
                "@5\n"+
                "D=D+A\n"+
                "//D = *addr\n"+
                "A=D\n"+
                "D=M\n"+
                "// *SP = *addr\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
            if(segment.equals("pointer")){
                if(index==0){
                    //D=This
                    file.writeBytes("@THIS\n");
                }if(index==1){
                    file.writeBytes("@THAT\n");
                }
                file.writeBytes("//D=THIS/THAT\n"+
                "D=M\n"+
                "// *SP =THIS/THAT\n"+
                "@SP\n"+
                "A=M\n"+
                "M=D\n"+
                "//SP++\n"+
                "@SP\n"+
                "M=M+1\n");
            }
        }

        if (command == C_POP) {
            if (segment.equals("local")) {
                file.writeBytes("//addr = LCL + i\n"+
                "@"+ Integer.toString(index) + "\n"+
                "D=A\n"+
                "@LCL\n"+
                "D=M+D\n"+
                "//store addr in R13\n"+
                "@13\n"+
                "M=D\n"+
                "//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "// *addr = *SP\n"+
                "@13\n"+
                "A=M\n"+
                "M=D\n");
            }
            else if (segment.equals("argument")) {
                file.writeBytes("//addr = ARG + i\n"+
                "@"+ Integer.toString(index) + "\n"+
                "D=A\n"+
                "@ARG\n"+
                "D=M+D\n"+
                "//store addr in R13\n"+
                "@13\n"+
                "M=D\n"+
                "//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "// *addr = *SP\n"+
                "@13\n"+
                "A=M\n"+
                "M=D\n");
            }
            else if (segment.equals("this")) {
                file.writeBytes("//addr = THIS + i\n"+
                "@"+ Integer.toString(index) + "\n"+
                "D=A\n"+
                "@THIS\n"+
                "D=M+D\n"+
                "//store addr in R13\n"+
                "@13\n"+
                "M=D\n"+
                "//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "// *addr = *SP\n"+
                "@13\n"+
                "A=M\n"+
                "M=D\n");
            }
            else if (segment.equals("that")) {
                file.writeBytes("//addr = THAT + i\n"+
                "@"+ Integer.toString(index) + "\n"+
                "D=A\n"+
                "@THAT\n"+
                "D=M+D\n"+
                "//store addr in R13\n"+
                "@13\n"+
                "M=D\n"+
                "//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "// *addr = *SP\n"+
                "@13\n"+
                "A=M\n"+
                "M=D\n");
            }
            else if (segment.equals("static")) {
                file.writeBytes("//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "//*static+i = *SP\n"+
                "@" + fileName +"."+ Integer.toString(index) + "\n"+
                "M=D\n");
            }
            else if (segment.equals("temp")) {
                file.writeBytes("//addr = 5 + i\n"+
                "@"+ Integer.toString(index) + "\n"+
                "D=A\n"+
                "D=D+1\n"+
                "D=D+1\n"+
                "D=D+1\n"+
                "D=D+1\n"+
                "D=D+1\n"+
                "//store addr in R13\n"+
                "@13\n"+
                "M=D\n"+
                "//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "// *addr = *SP\n"+
                "@13\n"+
                "A=M\n"+
                "M=D\n");
            }
            else if (segment.equals("pointer")) {
                file.writeBytes("//SP--\n"+
                "@SP\n"+
                "M=M-1\n"+
                "//D = *SP\n"+
                "@SP\n"+
                "A=M\n"+
                "D=M\n"+
                "//*THIS//*THAT = *SP\n");
                if (index == 0) {
                    file.writeBytes("//*THIS = *SP\n"+
                    "@THIS\n"+
                    "M=D\n");
                }
                else {
                    file.writeBytes("//*THAT = *SP\n"+
                    "@THAT\n"+
                    "M=D\n");
                }
            }
        }
    }
    public void close()throws IOException{
        this.file.close();
    }

}



