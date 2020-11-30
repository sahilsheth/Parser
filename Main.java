import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static PrintStream o;
    // Store current System.out before assigning a new value
    public static PrintStream console = System.out;

//    static {
//        try {
//            o = new PrintStream(new File("A.txt"));
//            System.setOut(o);

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    // Assign o to output stream

    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        StringBuilder fileContents = new StringBuilder();


        try {
            File file = new File(fileName);
            o = new PrintStream(new File(fileName.replace(".txt", "") + "_output.txt"));
            System.setOut(o);
//            File file = new File("inputTwo.txt");
//            File file = new File("inputThree.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContents.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("FIle not found.");
            e.printStackTrace();
        }

        String input = String.valueOf(fileContents);
        input.replaceAll("\\s", "");
        //remove all the comments
        input = input.replaceAll("(?s:/\\*.*?\\*/)|//.*", "");
        Lexer lex = new Lexer(input);
        ArrayList<Lexer.Token> tokens = Lexer.lexFunc(input);
        Parser parser = new Parser(tokens, o);
    }
}
