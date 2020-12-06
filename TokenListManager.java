import java.io.PrintStream;
import java.util.ArrayList;

public class TokenListManager {
    public boolean isFinished = false;
    public int currentIndex = 0;
    public PrintStream o;
    ArrayList<Lexer.Token> tokens;
    ArrayList<Lexer.Token> newTokens;

    public TokenListManager(ArrayList<Lexer.Token> tk, PrintStream o) {
        this.o = o;
        tokens = new ArrayList<>();
        newTokens = new ArrayList<>();
        for (int i = 0; i < tk.toArray().length; ++i) {
            tk.get(i).giveIndexNumber(i);
            tokens.add(tk.get(i));
        }
        System.out.println(tokens.toString());
        System.setOut(o);

    }




    public Lexer.Token getCurrentToken() {

        return tokens.get(this.currentIndex);
    }

    public void tempNextToken(int amount) {
        System.out.println("the token " + amount + " away from it is: ");
        int lol = currentIndex;
        System.out.print(tokens.get(lol + (amount)).toStringNew() + "\n");
    }

    public Lexer.Token getNextToken() {

        if (currentIndex > tokens.size() + 1) {
            isFinished = true;
            currentIndex--;
        }
        currentIndex++;
        return tokens.get(this.currentIndex);

    }


    public void addToNewArray(Lexer.Token newToken) {
        if ((newTokens.size() < 1) && (newToken != null)) {
            newTokens.add(newToken);
        } else {
            boolean isFound = false;
            for (Lexer.Token tk : newTokens) {
                if (newToken != null) {
                    if ((tk.data.equals(newToken.data) && tk.type.equals(newToken.type))) {
                        isFound = true;
                    }
                }
            }
            if(!isFound){
                newTokens.add(newToken);
            }
        }
    }


    public void printTokens() {
        for (Lexer.Token token : this.newTokens) {
            System.out.println("--==Tokens==--");
            System.out.println("Token: " + token.type + "\n Lexeme: " + token.data);
            token.printRules();
            System.out.println("  ");
            System.out.println("  ");

        }


    }

}
