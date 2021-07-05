import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * The JackTokenizer handles the compiler’s input. Provides services for: •
 * Ignoring white space • Getting the current token and advancing the input just
 * beyond it • Getting the type of the current token
 */
public class JackTokenizer {
    // Constants

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

    /**
     * Opens the input file and set it to be tokenized.
     * 
     * @param file
     * @throws IOException
     */
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
        // Init symbol set
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
        // Init keywordSet
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

    /**
     * Check if there is more tokens to handle in the input file
     * 
     * @return boolean if there is more or not
     * @throws IOException
     */
    public boolean hasMoreTokens() throws IOException {
        token=st.nextToken();
        return token!=StreamTokenizer.TT_EOF;
    }

    /**
     * Gets the next token from the input , and assemble it as the currentoken. This
     * method should be called only if hasMoreTokens is TRUE.
     * 
     * @throws IOException
     */
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
                    currentToken = stringConvert();
                }
            }
        }
    }

    /**
     * In case if the token is String this method extracting the string
     * 
     * @return string : the string on the currenttoken
     */
    private String stringConvert() throws IOException {
        String str = "\"";
        this.token = st.nextToken();
        st.ordinaryChar(' ');

        while (token != '\"') {
            if (st.ttype == StreamTokenizer.TT_NUMBER) {
                str += "" + (int) st.nval;
            } else {
                if (st.ttype == StreamTokenizer.TT_WORD) {
                    str += "" + st.sval;
                } else {
                    str += "" + (char) token;
                }
            }
            this.token = st.nextToken();
        }
        st.whitespaceChars(' ', ' ');
        return str + "\"";
    }

    /**
     * Returns the CONSTNT type of the current token
     * 
     * @return Int
     */
    public int tokenType() {
        if (st.ttype == StreamTokenizer.TT_NUMBER) {
            return INT_CONST;
        } else if (currentToken.charAt(0) == '\"') {
            return STRING_CONST;
        } else if (currentToken.length() == 1) {
            char ch = currentToken.charAt(0);
            if (symbolSet.contains(ch)) {
                return SYMBOL;
            }
            return IDENTIFIER;
        } else if (keywordSet.containsKey(currentToken)) {
            return KEYWORD;
        }
        return IDENTIFIER;
    }

    // currentoken Getter
    public String getCurrentToken() {
        if (this.tokenType() == STRING_CONST) {
            return this.currentToken.substring(1, this.currentToken.length() - 1);
        }
        return this.currentToken;
    }

    /**
     * Returns the keyboard which is the current token, as a constant. Called id the
     * token is KEYBOARD
     * 
     * @return Int - of the Constan's type from the keyboard set
     */
    public int keyWord() {
        return keywordSet.get(currentToken);
    }

    /**
     * Returns the charecter in the current token. Will be called it the current
     * token is SYMBOL
     * 
     * @return char of the charecter
     */
    public char symbol() {
        return currentToken.charAt(0);
    }

    /**
     * returns the string of the current token Called if the token is STRING
     * 
     * @return string
     */
    public String identifier() {
        return currentToken;
    }

    /**
     * Returns the int of the current token Called if the token is INT_CONST
     * 
     * @return
     */
    public int intVal() {
        return Integer.parseInt(currentToken);
    }

    /**
     * Returns the string value of the current token. with no quotes. Called if the
     * token is STRING_CONST.
     * 
     * @return
     */
    public String stringVal() {
        return currentToken.substring(1, currentToken.length() - 1);
    }

}
