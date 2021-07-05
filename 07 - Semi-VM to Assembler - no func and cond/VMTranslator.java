import java.io.IOException;

public class VMTranslator {
    public static void main(String args[])throws IOException {
        Parser parser=new Parser(args[0]);
        CodeWriter codeWriter=new CodeWriter(args[0]);
        String arg1="";
        int arg2=0;

        while (parser.hasMoreLines()){
            parser.advance();
            int commandType=parser.commandType();
            if (commandType==-1){
                break;
            }
            arg1=parser.arg1();
            if(commandType==2){
                codeWriter.writeArithmetic(arg1);
            }else {
                arg2=parser.arg2();
                codeWriter.writePushPop(commandType,arg1,arg2);
            }
        }
        codeWriter.close();
    }
}
