package fr.utbm.parser.tokenizer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Scanner implements Serializable {

    private Path path;
    public Tokenizer tokenizer;

    public Scanner(Path path) throws IOException {
        this.path = path;
        this.tokenizer = new Tokenizer(Files.newBufferedReader(path, StandardCharsets.UTF_8));
    }

    public Path getPath(){
        return this.path;
    }

    public void setPath(Path path){
        this.path = path;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.tokenizer);
    }

    public void getResult(){
        for(Token token : tokenizer.getTokens()){
            System.out.print(token.toString() + " ");
        }
    }
}
