import javax.imageio.IIOException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HackAssembler {
    // constans for type instruction:
    // A for @ - decimal num or a symbol
    public static int A_INSTRUCTION = 0;
    // C for : dest=comp;jump
    public static int C_INSTRUCTION = 1;
    // L for symbol
    public static int L_INSTRUCTION = 2;

    public static void main(String[] args) throws IOException {
        // initialize objects withe the given file name;
        String fileName = args[0];
        String outputFile = extractFileName(fileName);
        Parser parser = new Parser(fileName);
        Code coder = new Code();
        SymbolTable table = new SymbolTable();
        

        firstPass(parser, table);
        secPass(parser, coder, table, outputFile);

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
            if (c == '\\') {
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
        outputFile = "" + outputFile.substring(0, dot) + '.' + "hack";
        return outputFile;
    }

    /**
     * Reads all the program lines and focusing in (label) declarations and add the
     * new labels to the symboltable
     * 
     * @param parser
     * @param table
     * @throws IOException
     */
    public static void firstPass(Parser parser, SymbolTable table) throws IOException {
        // reset the parser
        parser.file.seek(0);
        parser.current_instruction = "";
        
        int instruction_couter = 0;
        // Run over the file line through the parser
        while (parser.hasMoreLines()) {
            parser.advance();
            // if it is a label instruction we will hadle the lable
            if (parser.instructionType() == L_INSTRUCTION) {
                String symbol = parser.symbol();
                if (!table.contains(symbol)) {
                    table.addEntry(symbol, instruction_couter);
                }
            }
            else {
                instruction_couter++;
            }
        }
    }
    
    /**
     * converts a given int to string of 16 binary code 
     * @param num
     * @return string with length of 16 {0,1}
     */
    public static String ConvertBinary(int num) {
        String binaryCode = Integer.toBinaryString(num);
        while (binaryCode.length() < 16) {
            binaryCode = "0" + binaryCode;
        }
        return binaryCode;
    }

    /**
     * Starting all over again for the begining of the file
     * get every instuction and parse it according to the instruction type.
     * and translate the parse to binary value
     * then , assembles it to string of 16 bits {0,1}, and write it to the outputFile
     *  
     * @param parser on the input file
     * @param coder Code tables
     * @param table : symolTable
     * @param outputFile file with the binary codes (".HACK")
     * @throws IOException
     */
    public static void secPass(Parser parser, Code coder, SymbolTable table, String outputFile) throws IOException {
        
        RandomAccessFile file = new RandomAccessFile(outputFile, "rw");
        file.seek(0);
        //  use the memory from register 16
        int memoryAdress = 16;
        String instruction = "";

        // reset the parser
        parser.file.seek(0);
        parser.current_instruction = "";

        while (parser.hasMoreLines()) {
            parser.advance();
            int type = parser.instructionType();
            if (type == C_INSTRUCTION) {
                // builds the C instruction:
                instruction = "111" + coder.comp(parser.comp()) + coder.dest(parser.dest()) + coder.jump(parser.jump())
                        + "\n";
                file.writeBytes(instruction);
            }
            else if (type == A_INSTRUCTION) {
                String symbol = parser.symbol();
                // handle Lable:
                if (symbol.charAt(0) < '0' || symbol.charAt(0) > '9') {
                    if (!table.contains(symbol)) {
                        table.addEntry(symbol, memoryAdress++);
                    }
                    // builds the A instruction:
                    instruction = ConvertBinary(table.getAddress(symbol)) + "\n";
                    file.writeBytes(instruction);
                } else {
                    // builds the L instruction:
                    instruction = ConvertBinary(Integer.parseInt(symbol)) + "\n";
                    file.writeBytes(instruction);
                }
            }
        }
        file.close();
    }
    
}