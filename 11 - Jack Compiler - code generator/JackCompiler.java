import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 * Usage:
 * prompt> JackCompiler input
 * 
 * Input:
 * *fileName.jack: name of a single source file, or
 * *folderName: name of a folder containing one or more .jack source files

 * Output:
 * *if the input is a single file: fileName.vm
 * *if the input is a folder: one .vm file for every .jack file, stored in the same folder
 */
public class JackCompiler {
    /**
     * For each source .jack file, the compiler creates a JackTokenizer and an output .vm file
     * Next, the compiler uses the SymbolTable, CompilationEngine, and VMWriter modules
     * to write the VM code into the output .vm file. 
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        File file = new File(args[0]);
        if (!file.isDirectory()) {
            String filename = file.getPath();
            int index = filename.lastIndexOf('.');
            filename = filename.substring(0, index);
            JackTokenizer jackTokenizer = new JackTokenizer(file);
            //Creating new file with the same name and extantion ".vm"
            RandomAccessFile rf = new RandomAccessFile(filename + ".vm", "rw");
            VMWriter out = new VMWriter(rf);
            SymbolTable st = new SymbolTable();
            CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, out, st);
            //Iteration
            if (jackTokenizer.hasMoreTokens()) {
                jackTokenizer.advance();
                compilationEngine.CompileClass();
            }
            rf.close();
        } else { // if this is dir go over all ".jack" files
            
            SymbolTable st;
            for (File f : file.listFiles()) {
                String filename = f.getPath();
                int index = filename.lastIndexOf('.');
                String extention = filename.substring(index + 1);
                if (extention.equals("jack")) {
                    filename = filename.substring(0, index);
                    JackTokenizer jackTokenizer = new JackTokenizer(f);
                    RandomAccessFile rf = new RandomAccessFile(filename + ".vm", "rw");
                    VMWriter out = new VMWriter(rf);
                    st = new SymbolTable();
                    CompilationEngine compilationEngine = new CompilationEngine(jackTokenizer, out, st);
                    //Iteration
                    if (jackTokenizer.hasMoreTokens()) {
                        jackTokenizer.advance();
                        compilationEngine.CompileClass();
                    }
                    rf.close();
                }
            }
        }
    }
}