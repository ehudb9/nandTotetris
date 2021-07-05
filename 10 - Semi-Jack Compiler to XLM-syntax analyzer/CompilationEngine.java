import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Hashtable;
import javax.imageio.IIOException;



public class CompilationEngine {
    JackTokenizer tokenaizer;
    RandomAccessFile out;
    Hashtable<Integer,String> tokentypeSet;
    Hashtable<Integer,String> keywordSet;

    static int KEYWORD=1;
    static int SYMBOL=2;
    static int IDENTIFIER=3;
    static int INT_CONST=4;
    static int STRING_CONST=5;
    static int CLASS=6;
    static int METHOD=7;
    static int FUNCTION=8;
    static int CONSTRUCTOR=9;
    static int INT=10;
    static int BOOLEAN=11;
    static int CHAR=12;
    static int VOID=13;
    static int VAR=14;
    static int STATIC=15;
    static int FIELD=16;
    static int LET=17;
    static int DO=18;
    static int IF=19;
    static int ELSE=20;
    static int WHILE=21;
    static int RETURN=22;
    static int TRUE=23;
    static int FALSE=24;
    static int NULL=25;
    static int THIS=26;


    public CompilationEngine(JackTokenizer in, RandomAccessFile out) throws IOException{
        this.tokenaizer = in;
        this.out = out;
        this.tokentypeSet = new Hashtable <>();
        tokentypeSet.put(KEYWORD,"keyword");
        tokentypeSet.put(SYMBOL,"symbol");
        tokentypeSet.put(IDENTIFIER,"identifier");
        tokentypeSet.put(INT_CONST,"integerConstant");
        tokentypeSet.put(STRING_CONST,"stringConstant");

        this.keywordSet = new Hashtable<>();
        keywordSet.put(CLASS,"class");
        keywordSet.put(CONSTRUCTOR,"constructor");
        keywordSet.put(FUNCTION,"function");
        keywordSet.put(METHOD,"method");
        keywordSet.put(FIELD,"field");
        keywordSet.put(STATIC,"static");
        keywordSet.put(VAR,"var");
        keywordSet.put(CLASS,"class");
        keywordSet.put(INT,"int");
        keywordSet.put(CHAR,"char");
        keywordSet.put(BOOLEAN,"boolean");
        keywordSet.put(VOID,"void");
        keywordSet.put(TRUE,"true");
        keywordSet.put(FALSE,"false");
        keywordSet.put(NULL,"null");
        keywordSet.put(THIS,"this");
        keywordSet.put(DO,"do");
        keywordSet.put(IF,"if");
        keywordSet.put(ELSE,"else");
        keywordSet.put(WHILE,"while");
        keywordSet.put(RETURN,"return");
        keywordSet.put(LET,"let");
    }

    private void process(String str) throws IOException {
        if (tokenaizer.getCurrentToken().equals(str)) {
            printXMLToken();
        }
        else {
            System.out.println(tokenaizer.getCurrentToken());
            System.out.println(str);
            System.out.println("Syntax Error");
        }
        if(tokenaizer.hasMoreTokens()) {
            tokenaizer.advance();
        }
    }

    private void printXMLToken() throws IOException{
        String line="";
        int curTokenType;
        curTokenType=tokenaizer.tokenType();
        line="<"+tokentypeSet.get(curTokenType)+"> ";
        if(curTokenType==KEYWORD){
            line+=keywordSet.get(tokenaizer.keyWord());
        }if(curTokenType==SYMBOL){
            if(tokenaizer.getCurrentToken().equals("<")){
                line+="&lt;";
            }else if(tokenaizer.getCurrentToken().equals(">")){
                line+="&gt;";
            }else if(tokenaizer.getCurrentToken().equals("&")){
                line+="&amp;";
            }else{
                line+=tokenaizer.symbol();
            }
        }if(curTokenType==IDENTIFIER){
            line+=tokenaizer.identifier();
        }if(curTokenType==INT_CONST){
            line+=tokenaizer.intVal();
        }if(curTokenType==STRING_CONST){
            line+=tokenaizer.stringVal();
        }
        line+=" </"+tokentypeSet.get(curTokenType)+">";
        out.writeBytes(line+"\n");
    }


    public void CompileClass()throws IOException {
        out.writeBytes("<class>\n");
        process("class");
        process(tokenaizer.identifier());
        process("{");
        while(tokenaizer.getCurrentToken().equals("static")||tokenaizer.getCurrentToken().equals("field")){
            this.CompileClassVarDec();
        }
        while (tokenaizer.getCurrentToken().equals("constructor")||tokenaizer.getCurrentToken().equals("function")||tokenaizer.getCurrentToken().equals("method")){
            this.CompileSubroutineDec();
        }
        process("}");
        out.writeBytes("</class>\n");
    }
    public void CompileClassVarDec() throws IOException{
        out.writeBytes("<classVarDec>\n");
        String temp = keywordSet.get(tokenaizer.keyWord());
        process(temp);
        if(tokenaizer.tokenType()==KEYWORD){
            temp = keywordSet.get(tokenaizer.keyWord());
            process(temp);
        }else{
            temp=tokenaizer.identifier();
            process(temp);
        }
        while(!tokenaizer.getCurrentToken().equals(";")) {
            if(tokenaizer.getCurrentToken().equals(",")){
                process(",");
            }
            else {
                process(tokenaizer.identifier());
            }
        }
        process(";");
        out.writeBytes("</classVarDec>\n");
    }

    public void CompileSubroutineDec()throws IOException {
        out.writeBytes("<subroutineDec>\n");
        //('constructor'|'function'|'method')
        String temp = keywordSet.get(tokenaizer.keyWord());
        process(temp);
        //('void|type')
        if(tokenaizer.tokenType()==KEYWORD){
            temp = keywordSet.get(tokenaizer.keyWord());
            process(temp);
        }else{
            temp=tokenaizer.identifier();
            process(tokenaizer.identifier());
        }
        process(tokenaizer.identifier());
        process("(");
        this.CompileParameterList();
        process(")");
        this.CompileSubroutineBody();
        out.writeBytes("</subroutineDec>\n");
    }
    public void CompileParameterList()throws IOException {
        out.writeBytes("<parameterList>\n");
        boolean secparam=false;
        while (!tokenaizer.getCurrentToken().equals(")")){
            if(secparam){
                process(",");
            }
            if(tokenaizer.tokenType()==KEYWORD){
                process(keywordSet.get(tokenaizer.keyWord()));
            }else{
                process(tokenaizer.identifier());
            }
            process(tokenaizer.identifier());
            if(!secparam){
                secparam=true;
            }
        }
        out.writeBytes("</parameterList>\n");
    }
    public void CompileSubroutineBody()throws IOException {
        out.writeBytes("<subroutineBody>\n");
        process("{");
        while(tokenaizer.getCurrentToken().equals("var")){
            this.CompileVarDec();
        }
        this.CompileStatements();
        process("}");
        out.writeBytes("</subroutineBody>\n");
    }

    public void CompileVarDec() throws IOException{
        out.writeBytes("<varDec>\n");
        process("var");
        if(tokenaizer.tokenType()==KEYWORD){
            process(keywordSet.get(tokenaizer.keyWord()));
        }else{
            process(tokenaizer.identifier());
        }
        process(tokenaizer.identifier());
        while(!tokenaizer.getCurrentToken().equals(";")) {
            process(",");
            process(tokenaizer.identifier());
        }
        process(";");
        out.writeBytes("</varDec>\n");
    }

    public void CompileStatements()throws IOException {
        out.writeBytes("<statements>\n");
        String statement_dec;
        if(tokenaizer.tokenType()==KEYWORD) {
            statement_dec = keywordSet.get(tokenaizer.keyWord());
            while (statement_dec.equals("let") || statement_dec.equals("if") || statement_dec.equals("while") || statement_dec.equals("do") || statement_dec.equals("return")) {
                switch (statement_dec) {
                    case "let":
                        this.CompileLet();
                        break;
                    case "if":
                        this.CompileIf();
                        break;
                    case "while":
                        this.CompileWhile();
                        break;
                    case "do":
                        this.CompileDo();
                        break;
                    case "return":
                        this.CompileReturn();
                        break;
                }
                if(tokenaizer.tokenType()==KEYWORD) {
                    statement_dec = keywordSet.get(tokenaizer.keyWord());
                }else{
                    break;
                }
            }
        }
        out.writeBytes("</statements>\n");
    }

    public void CompileLet() throws IOException{
        out.writeBytes("<letStatement>\n");
        process("let");
        process(tokenaizer.identifier());
        if(tokenaizer.getCurrentToken().equals("[")){
            process("[");
            this.CompileExpression();
            process("]");
        }
        process("=");
        CompileExpression();
        process(";");
        out.writeBytes("</letStatement>\n");
    }

    public void CompileIf() throws IOException{
        out.writeBytes("<ifStatement>\n");
        process("if");
        process("(");
        this.CompileExpression();
        process(")");
        process("{");
        this.CompileStatements();
        process("}");
        if(tokenaizer.tokenType()==KEYWORD){
            if(keywordSet.get(tokenaizer.keyWord()).equals("else")){
                process("else");
                process("{");
                this.CompileStatements();
                process("}");
            }
        }
        out.writeBytes("</ifStatement>\n");
    }

    public void CompileWhile() throws IOException{
        out.writeBytes("<whileStatement>\n");
        process("while");
        process("(");
        this.CompileExpression();
        process(")");
        process("{");
        this.CompileStatements();
        process("}");
        out.writeBytes("</whileStatement>\n");
    }

    public void CompileDo() throws IOException {
        out.writeBytes("<doStatement>\n");
        process("do");
        process(tokenaizer.identifier());
        if(tokenaizer.getCurrentToken().equals(".")){
            process(".");
            process(tokenaizer.identifier());
            process("(");
            this.CompileExpressionList();
            process(")");
        }else if(tokenaizer.getCurrentToken().equals("(")){
            process("(");
            this.CompileExpressionList();
            process(")");
        }
        //this.CompileTerm();
        process(";");
        out.writeBytes("</doStatement>\n");
    }
    public void CompileReturn() throws IOException {
        out.writeBytes("<returnStatement>\n");
        process("return");
        if (!tokenaizer.getCurrentToken().equals(";")){
            this.CompileExpression();
        }
        process(";");
        out.writeBytes("</returnStatement>\n");
    }
    public void CompileExpression()throws IOException {
        out.writeBytes("<expression>\n");
        this.CompileTerm();
        while (!tokenaizer.getCurrentToken().equals(",") && !tokenaizer.getCurrentToken().equals(";") && !tokenaizer.getCurrentToken().equals(")") && !tokenaizer.getCurrentToken().equals("]")){
            //System.out.println(tokenaizer.getCurrentToken().equals(")"));
            if(tokenaizer.getCurrentToken().equals("+")){
                process("+");
            }
            else if(tokenaizer.getCurrentToken().equals("-")){
                process("-");
            }
            else if(tokenaizer.getCurrentToken().equals("*")){
                process("*");
            }
            else if(tokenaizer.getCurrentToken().equals("/")){
                process("/");
            }
            else if(tokenaizer.getCurrentToken().equals("&")){
                process("&");
            }
            else if(tokenaizer.getCurrentToken().equals("|")){
                process("|");
            }
            else if(tokenaizer.getCurrentToken().equals("<")){
                process("<");
            }
            else if(tokenaizer.getCurrentToken().equals(">")){
                process(">");
            }
            else if(tokenaizer.getCurrentToken().equals("=")){
                process("=");
            }
            this.CompileTerm();
        }
        out.writeBytes("</expression>\n");
    }
    public void CompileTerm() throws IOException {
        out.writeBytes("<term>\n");
        if (tokenaizer.tokenType() == SYMBOL) {
            if (tokenaizer.symbol() == '~') {
                process("~");
                this.CompileTerm();

            } else if (tokenaizer.symbol() == '-') {
                process("-");
                this.CompileTerm();
            }
        }
        if (tokenaizer.tokenType() == SYMBOL) {
            if (tokenaizer.symbol() == '(') {
                process("(");
                this.CompileExpression();
                process(")");
            }
        } else if (tokenaizer.tokenType() == INT_CONST) {
            process("" + tokenaizer.intVal());
        } else if (tokenaizer.tokenType() == STRING_CONST) {
            process(tokenaizer.stringVal());
        } else if (tokenaizer.tokenType() == KEYWORD) {
            String key = keywordSet.get(tokenaizer.keyWord());
            if (key.equals("true") || key.equals("null") || key.equals("false") || key.equals("this")) {
                process(key);
            }
        } else if (tokenaizer.tokenType() == IDENTIFIER) {
            process(tokenaizer.identifier());
            if (tokenaizer.getCurrentToken().equals("[")) {
                process("[");
                this.CompileExpression();
                process("]");
            } else if (tokenaizer.getCurrentToken().equals(".")) {
                process(".");
                process(tokenaizer.identifier());
                process("(");
                this.CompileExpressionList();
                process(")");
            } else if (tokenaizer.getCurrentToken().equals("(")) {
                process("(");
                this.CompileExpressionList();
                process(")");
            }

        }
        out.writeBytes("</term>\n");
    }

    public void CompileExpressionList() throws IOException{
        out.writeBytes("<expressionList>\n");
        boolean moreThenOneex=false;
        while(!tokenaizer.getCurrentToken().equals(")")){
            if(moreThenOneex){
                process(",");
            }
            this.CompileExpression();
            moreThenOneex=true;
        }
        out.writeBytes("</expressionList>\n");
    }
}
