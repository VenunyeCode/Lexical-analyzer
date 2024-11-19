package fr.utbm.parser.tokenizer;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Objects;


public class Tokenizer implements Closeable {
    @Override
    public void close() throws IOException {

    }

    private final ArrayList<Token> tokens = new ArrayList<>();
    private static boolean isWhiteSpace(int ch){
        return Character.isWhitespace(ch);
    }

    private static boolean isDigit(int ch){
        return ch >= '0' && ch <= '9';
    }

    private static boolean isAlpha(int ch){
        return ch >= 'A' && ch <= 'Z';
    }

    private final Reader reader;

    public Tokenizer(Reader reader)throws IOException{
        this.reader = reader;
        nextToken();
    }

    public ArrayList<Token> getTokens(){
        return this.tokens;
    }

    public void setTokens(Token t){
        tokens.add(t);
    }

    public Tokenizer(String str) throws IOException {
        this.reader = new StringReader(str);
        nextToken();
    }

    private int peek() throws IOException {
        reader.mark(1);
        try {
            return reader.read();
        } finally {
            reader.reset();
        }
    }

    public void nextToken() throws IOException{
        int col = 0, row = 0, line = 0;

        for (;;){
            int ch = reader.read();
            if(ch == -1){
                setTokens(new Token(Type.EOF));
                break;
            }
            else if(ch == '\n'){
                setTokens(new Token(Type.LF));
                line++;
            }
            else if(ch == '+')
                setTokens(new Token(Type.PLUS));
            else if(ch == '-')
                setTokens(new Token(Type.MINUS));
            else if(ch == '*')
                setTokens(new Token(Type.MULT));
            else if(ch == '/')
                setTokens(new Token(Type.DIV));
            else if(ch == '=')
                setTokens(new Token(Type.EQ));
            else if(ch == '>' || ch == '<')
                nextRelationalOperatorToken(ch);
            else if(isAlpha(ch)) nextKeyWordToken(ch);
            else if(isDigit(ch)) nextNumberToken(ch);
            else if(ch == ',')
                setTokens(new Token(Type.COMMA));
            else if(!isWhiteSpace(ch))
                throw new IOException("Unexpected character: " +ch + "at line " + line + " col: " + col + ", row: " + row);
            col++; row++;
        }
    }

    private boolean doesStringMatchAKeyWord(String value){
        KeyWord[] keyWords = KeyWord.values();
        for (KeyWord keyWord : keyWords){
            if(Objects.equals(value, keyWord.name())){
                return true;
            }
        }
        return false;
    }

    private void nextRelationalOperatorToken(int first) throws IOException {
        int second = peek();
        if(first == '>'){
            if(second == '='){
                reader.skip(1);
                setTokens(new Token(Type.GTE));
            }
            else if(second == '<'){
                reader.skip(1);
                setTokens(new Token(Type.NE));;
            }
            else {
                setTokens(new Token(Type.GT));
            }

        }
        else {
            assert first == '<';
            if(second == '='){
                reader.skip(1);
                setTokens(new Token(Type.LTE));
            } else if (second == '>') {
                reader.skip(1);
                setTokens(new Token(Type.NE));
            } else {
                setTokens(new Token(Type.LT));
            }
        }
    }

    private void nextKeyWordToken(int first) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append((char) first);
        for (;;) {
            int ch = peek();
            if(!isAlpha(ch))
                break;

            reader.skip(1);
            buf.append((char)ch);
        }
        if(doesStringMatchAKeyWord(buf.toString()))
            setTokens(new Token(Type.KEYWORD, buf.toString()));
        else
            setTokens(new Token(Type.ID, buf.toString()));
    }

    private void nextNumberToken(int first) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append((char) first);
        for(;;){
            int ch = peek();
            if(!isDigit(ch))
                break;
            reader.skip(1);
            buf.append((char) ch);
        }

        setTokens(new Token(Type.NUMBER, buf.toString()));
    }

}
