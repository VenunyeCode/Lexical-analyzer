package fr.utbm.parser.tokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Path path = Paths.get("/Users/kokousogbo/Downloads/Parser/src/main/tiny_examples/test1.tb");
        try {
            Scanner scanner = new Scanner(path);
            scanner.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
