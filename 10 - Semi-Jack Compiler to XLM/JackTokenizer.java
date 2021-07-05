import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;

public class JackTokenizer {
    static int KEYWORD = 1;
    static int SYMBOL = 2;
    static int IDENTIFIER = 3;
    static int INT_CONST = 4;
    static int STRING_CONST = 5;
    static int CLASS = 6;
    static int METHOD = 7;
    static int FUNCTION = 8;
    static int CONSTRUCTOR = 9;
    static int INT = 10;
    static int BOOLEAN = 11;
    static int CHAR = 12;
    static int VOID = 13;
    static int VAR = 14;
    static int STATIC = 15;
    static int FIELD = 16;
    static int LET = 17;
    static int DO = 18;
    static int IF = 19;
    static int ELSE = 20;
    static int WHILE = 21;
    static int RETURN = 22;
    static int TRUE = 23;
    static int FALSE = 24;
    static int NULL = 25;
    static int THIS = 26;

    StreamTokenizer st;
    String currentToken;
    HashSet<Character> symbolSet;
    Hashtable<String, Integer> keywordSet;
    int token;
    
    public JackTokenizer(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        this.st = new StreamTokenizer(new BufferedReader(new InputStreamReader(in)));
        st.slashSlashComments(true);
        st.slashStarComments(true);
        st.wordChars('_', '_');
        st.ordinaryChar('.');
        st.ordinaryChar('-');
        st.ordinaryChar('\"');
        st.ordinaryChar('/');
        this.currentToken = null;
        this.token = 0;
        // init symbol set
        this.symbolSet = new HashSet<>();
        symbolSet.add('}');
        symbolSet.add('{');
        symbolSet.add('(');
        symbolSet.add(')');
        symbolSet.add('[');
        symbolSet.add(']');
        symbolSet.add('.');
        symbolSet.add(',');
        symbolSet.add(';');
        symbolSet.add('+');
        symbolSet.add('-');
        symbolSet.add('*');
        symbolSet.add('/');
        symbolSet.add('&');
        symbolSet.add('|');
        symbolSet.add('>');
        symbolSet.add('<');
        symbolSet.add('=');
        symbolSet.add('~');
        // init keyword set
        this.keywordSet = new Hashtable<>();
        keywordSet.put("class", CLASS);
        keywordSet.put("constructor", CONSTRUCTOR);
        keywordSet.put("function", FUNCTION);
        keywordSet.put("method", METHOD);
        keywordSet.put("field", FIELD);
        keywordSet.put("static", STATIC);
        keywordSet.put("var", VAR);
        keywordSet.put("class", CLASS);
        keywordSet.put("int", INT);
        keywordSet.put("char", CHAR);
        keywordSet.put("boolean", BOOLEAN);
        keywordSet.put("void", VOID);
        keywordSet.put("true", TRUE);
        keywordSet.put("false", FALSE);
        keywordSet.put("null", NULL);
        keywordSet.put("this", THIS);
        keywordSet.put("let", LET);
        keywordSet.put("do", DO);
        keywordSet.put("if", IF);
        keywordSet.put("else", ELSE);
        keywordSet.put("while", WHILE);
        keywordSet.put("return", RETURN);
    }

    public boolean hasMoreTokens() throws IOException {
        token = st.nextToken();
        return token != StreamTokenizer.TT_EOF;
    }

    public void advance() throws IOException {
        if (st.ttype == StreamTokenizer.TT_NUMBER) {
            currentToken = "" + (int) st.nval;
        } else {
            if (st.ttype == StreamTokenizer.TT_WORD) {
                currentToken = "" + st.sval;
            } else {
                if (token != '\"') {
                    currentToken = "" + (char) token;
                } else {
                    currentToken = tokenIsString();
                }
            }
        }
    }

    private String tokenIsString() throws IOException {
        String stringConst = "\"";
        this.token = st.nextToken();
        st.ordinaryChar(' ');
        while (token != '\"') {
            if (st.ttype == StreamTokenizer.TT_NUMBER) {
                stringConst += "" + (int) st.nval;
            } else {
                if (st.ttype == StreamTokenizer.TT_WORD) {
                    stringConst += "" + st.sval;
                } else {
                    stringConst += "" + (char) token;
                }
            }
            this.token = st.nextToken();
        }
        st.whitespaceChars(' ', ' ');
        return stringConst + "\"";
    }

    public int tokenType() {
        if (st.ttype == StreamTokenizer.TT_NUMBER) {
            return INT_CONST;
        }
        if (currentToken.charAt(0) == '\"') {
            return STRING_CONST;
        }
        if (currentToken.length() == 1) {
            char ch = currentToken.charAt(0);
            if (symbolSet.contains(ch)) {
                return SYMBOL;
            }
            return IDENTIFIER;
        }
        if (keywordSet.containsKey(currentToken)) {
            return KEYWORD;
        }
        return IDENTIFIER;
    }

    public int keyWord() {
        return keywordSet.get(currentToken);
    }

    public char symbol() {
        return currentToken.charAt(0);
    }

    public String identifier() {
        return currentToken;
    }

    public int intVal() {
        return Integer.parseInt(currentToken);
    }

    public String stringVal() {
        return currentToken.substring(1, currentToken.length() - 1);
    }

    public String getCurrentToken() {
        if (this.tokenType() == STRING_CONST) {
            return this.currentToken.substring(1, this.currentToken.length() - 1);
        }
        return this.currentToken;
    }

}
