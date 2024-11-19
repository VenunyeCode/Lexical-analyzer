package fr.utbm.parser.tokenizer;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class Token implements Serializable {
    private final Type type;
    private final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token(Type type){
        this.type = type;
        this.value = "";
    }

    public Type getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        if(this.value.isEmpty()) {
            return "<" + this.getType().toString() + ">";
        }
        else {
            return "<" + this.getType().toString() +  ", " + getValue() + ">";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Token token = (Token) obj;
        if(type != token.getType()) return false;
        if(!value.equals(token.getValue())) return false;

        return true;
    }
}
