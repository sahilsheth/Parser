/*


R1. <Rat20F>  ::=   <Opt Function Definitions>   $$  <Opt Declaration List>  <Statement List>  $$

R2. <Opt Function Definitions> ::= <Function Definitions>     |  <Empty>

R3. <Function Definitions>  ::= <Function> | <Function> <Function Definitions>

R4. <Function> ::= function  <Identifier>   ( <Opt Parameter List> )  <Opt Declaration List>  <Body>

R5. <Opt Parameter List> ::=  <Parameter List>    |     <Empty>

R6. <Parameter List>  ::=  <Parameter>    |     <Parameter> , <Parameter List>

R7. <Parameter> ::=  <IDs >  <Qualifier>

R8. <Qualifier> ::= int     |    boolean    |  real

R9. <Body>  ::=  {  < Statement List>  }

R10. <Opt Declaration List> ::= <Declaration List>   |    <Empty>

R11. <Declaration List>  := <Declaration> ;     |      <Declaration> ; <Declaration List>

R12. <Declaration> ::=   <Qualifier > <IDs>

R13. <IDs> ::=     <Identifier>    | <Identifier>, <IDs>

R14. <Statement List> ::=   <Statement>   | <Statement> <Statement List>

R15. <Statement> ::=   <Compound>  |  <Assign>  |   <If>  |  <Return>   | <Print>   |   <Scan>   |  <While>

R16. <Compound> ::=   {  <Statement List>  }

R17. <Assign> ::=     <Identifier> = <Expression> ;

R18. <If> ::=     if  ( <Condition>  ) <Statement>   fi   |

                  if  ( <Condition>  ) <Statement>   else  <Statement>  fi

R19. <Return> ::=  return ; |  return <Expression> ;

R20. <Print> ::=    put ( <Expression>);

R21. <Scan> ::=    get ( <IDs> );

R22. <While> ::=  while ( <Condition>  )  <Statement>

R23. <Condition> ::=     <Expression>  <Relop>   <Expression>

R24. <Relop> ::=        ==   |   !=    |   >     |   <    |  <=   |    =>

R25. <Expression>  ::=    <Expression> + <Term>    | <Expression>  - <Term>    |    <Term>

R26. <Term>    ::=      <Term>  *  <Factor>     |   <Term>  /  <Factor>     |     <Factor>

R27. <Factor> ::=      -  <Primary>    |    <Primary>

R28. <Primary> ::=     <Identifier>  |  <Integer>  |   <Identifier>  ( <IDs> )   |   ( <Expression> )   |

                                     <Real>  |   true   |  false

R29. <Empty>   ::= 



Note: <Identifier>, <Integer>, <Real> are token types as defined in section (1) above





 */



import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Parser {

    public static ArrayList<Lexer.Token> tokenArrayList;
    public TokenListManager manager;
    public boolean flag;
    List<Lexer.Token> tokensLinkedList;
    PrintStream o;


    public Parser(ArrayList<Lexer.Token> tokens, PrintStream o) {
        manager = new TokenListManager(tokens, o);
        this.o = o;
        PrintStream console = System.out;
        System.setOut(o);
        tokenArrayList = tokens;
        tokensLinkedList = convertALtoLL(tokenArrayList);
        flag = true;
        R1();
    }


    public static <T> List<T> convertALtoLL(List<T> aL) {

        return new LinkedList<>(aL);
    }

    public void R1() {
        String s = "Rat20F>  ::=   <Opt Function Definitions>   $$  <Opt Declaration List>  <Statement List>  $$";
        System.out.println(s);
//        manager.currentIndex = 0;
        Lexer.Token token = manager.getCurrentToken();
//        System.out.println("TOKEN: " + token.toString());
        token = R2(token);
        if (!token.data.equals("$$")) {
            printError(token, "$$");
        }

        token = R10(token);
        token = R14(token);
        if (!token.data.equals("$$")) {
            printError(token, "$$");
        }
        token.addToRules(s);
        manager.addToNewArray(token);
        token = R2(token);
        manager.addToNewArray(token);
        if (token.data.equals("$$")) {
            token = R10(token);
            manager.addToNewArray(token);
            R14(token);
        }


        manager.printTokens();

    }

    public Lexer.Token R2(Lexer.Token token) {
        String s = "Opt Function Definitions> ::= <Function Definitions>     |  <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);

//        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);


        if (!(token.data.equals("function"))) {
            return manager.getCurrentToken();
        }


        return R3(token);
    }

    public Lexer.Token R3(Lexer.Token token) {

        String s = "<Function Definitions>  ::= <Function> | <Function> <Function Definitions>   ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);


        R4(token);
        return R4EMPTY(token);
    }

    public void R4(Lexer.Token token) {
        String s = "Function> ::= function  <Identifier>   ( <Opt Parameter List> )  <Opt Declaration List>  <Body>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = R13(token);
        if (!token.data.equals("(")) {
            printError(token, "(");
        }
        token = R5(token);
        if (!token.data.equals(")")) {
            printError(token, ")");
        }
        token = R10(token);
        R9(token);
    }

    public Lexer.Token R4EMPTY(Lexer.Token token) {
        String s = "<Function Definitions>' ::= <Function Definitions> | <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = manager.getNextToken();
        if (token.type == Lexer.TokenType.KEYWORD) {
            return token;
        } else {
            return R3(token);
        }
    }

    public Lexer.Token R5(Lexer.Token token) {
        String s = "Opt Parameter List> ::=  <Parameter List>    |     <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
        manager.tempNextToken(-1);
        manager.tempNextToken(0);
        manager.tempNextToken(1);


        if (token.type != Lexer.TokenType.IDENTIFIER) {
            return token;
        }
        token = R6(token);
        manager.addToNewArray(token);
        return token;
    }

    public Lexer.Token R6(Lexer.Token token) {
        String s = "Parameter List>  ::=  <Parameter>    |     <Parameter> , <Parameter List>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        R7(token);
        return R5(manager.getNextToken());
    }

    public void R7(Lexer.Token token) {
        String s = "Parameter> ::=  <IDs >  <Qualifier> ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = R13(token);
        R8(token);

    }

    public void R8(Lexer.Token token) {
        String s = "Qualifier> ::= int     |    boolean    |  real ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        switch (token.data) {
            case "int":
            case "boolean":
            case "real":
                break;
            default:
                printError(token, "NUMBER TYPE");
                break;
        }
    }

    public void R9(Lexer.Token token) {
        String s = "<Body>  ::=  {  < Statement List>  }";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (!token.data.equals("{")) {
            printError(token, "{");
        }
        token = manager.getNextToken();
        token = R14(token);
        if (!token.data.equals("}")) {
            printError(token, "}");
        }
    }

    public Lexer.Token R10(Lexer.Token token) {
        String s = "<Opt Declaration List> ::= <Declaration List> | <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = manager.getNextToken();
        if (token.data.equals("int") || token.data.equals("boolean") || token.data.equals("real")) {
            return R11(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R11(Lexer.Token token) {
        String s = "<Declaration List>  := <Declaration> ;     |      <Declaration> ; <Declaration List>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = R12(token);
        if (!token.data.equals(";")) {
            printError(token, ";");
        }
        return R11Empty(manager.getNextToken());
    }

    public Lexer.Token R11Empty(Lexer.Token token) {
        String s = "<Declaration List> ::= <Declaration List>  |  <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("int") || token.data.equals("boolean") || token.data.equals("real")) {
            return R11(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R12(Lexer.Token token) {
        String s = "<Declaration> ::=   <Qualifier > <IDs> ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        R8(token);
        token = manager.getNextToken();
        return R13(token);
    }

    public Lexer.Token R13(Lexer.Token token) {
        String s = "<IDs> ::=     <Identifier>    | <Identifier>, <IDs>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (!(token.type.equals(Lexer.TokenType.IDENTIFIER))) {
            printError(token, "identifier");
            manager.tempNextToken(-1);
        }
        token = R13Empty(token);
        manager.addToNewArray(token);
        return token;

    }

    private Lexer.Token R13Empty(Lexer.Token token) {
        String s = "<IDs> ::=     <Identifier>    | <Identifier>, <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);
        token = manager.getNextToken();
        if (token.data.equals(",")) {
            return R13(manager.getNextToken());
        } else {
            return token;
//            return manager.getNextToken();
        }
    }

    private void printError(Lexer.Token token, String error) {
        System.out.println("Error at line number " + token.lineNumber + ": expected " + error + " but got " + token.data);
    }

    public Lexer.Token R14(Lexer.Token token) {
        String s = "<Statement List> ::=   <Statement>   | <Statement> <Statement List>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        R15(token);
        return R14Empty(manager.getNextToken());
    }

    private Lexer.Token R14Empty(Lexer.Token token) {
        String s = "<Statement List> ::= <Statement List>  |  <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("{") || token.type.equals(Lexer.TokenType.IDENTIFIER)
                || token.data.equals("if") || token.data.equals("return")
                || token.data.equals("put") || token.data.equals("get") || token.data.equals("while")) {
            token = R14(token);
            manager.addToNewArray(token);
        }
        return token;
    }

    public void R15(Lexer.Token token) {
        String s = "<Statement> ::=   <Compound>  |  <Assign>  |   <If>  |  <Return>   | <Print>   |   <Scan>   |  <While> ";
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);
        if (token.data.equals("{")) {
            R16(token);
        } else if (token.type == Lexer.TokenType.IDENTIFIER) {
            R17(token);
        } else if (token.data.equals("if")) {
            R18(token);
        } else if (token.data.equals("return")) {
            R19(token);
        } else if (token.data.equals("put")) {
            R20(token);
        } else if (token.data.equals("get")) {
            R21(token);
        } else if (token.data.equals("while")) {
            R22(token);
        } else {
            printError(token, "keyword or an identifier");
        }
    }

    public void R16(Lexer.Token token) {
        String s = "<Compound> ::=   {  <Statement List>  } ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);

        token = R14(manager.getNextToken());
        manager.addToNewArray(token);
        if (!token.data.equals("}")) {
            printError(token, "}");
        }
    }

    public void R17(Lexer.Token token) {
        String s = "<Assign> ::=     <Identifier> = <Expression> ;";
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);
//        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
        token = manager.getNextToken();
        if (!token.data.equals("=")) {
            printError(token, "=");
        }
        token = R25(manager.getNextToken());
        manager.addToNewArray(token);
        if (!token.data.equals(";")) {
            printError(token, ";");
        }
    }

    public void R18(Lexer.Token token) {
        String s = "<If> ::=     if  ( <Condition>  ) <Statement>   IF";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);

        //compareLexemes("(");
        if(compareLexemes("(")){
            R23(token);
        }
        //R23(token);
        //compareLexemes(")");
        if(compareLexemes(")")){
            R15(manager.getNextToken());
            R18FI(manager.getNextToken());
        }
        //R15(manager.getNextToken());
        //R18FI(manager.getNextToken());
    }

    public void R18FI(Lexer.Token token) {
        String s = "if  ( <Condition>  ) <Statement>   else  <Statement>  fi ";
        //token.addToRules(s);
        //manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("fi")) {
//            R18FI(manager.getNextToken());
        } else if (token.data.equals("else")) {
            R15(manager.getNextToken());
            compareLexemes("fi");
        }

    }

    private boolean compareLexemes(String data) {
        Lexer.Token comparer = manager.getNextToken();
        if (!comparer.data.equals(data)) {
            printError(comparer, data);
            return false;
        }
        return true;
    }

    public void R19(Lexer.Token token) {
        String s = "<Return> ::=  return ; |  return <Expression> ;";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        R19_SEMICOLON(token);
    }

    private void R19_SEMICOLON(Lexer.Token token) {
        String s = "<Return>' ::= ; | <Expression>;";
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);
        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
        if (token.data.equals(";")) {
            return;
        } else {
            token = R13(token);
            manager.addToNewArray(token);
        }
        if (!token.data.equals(";")) {
            printError(token, ";");
        }
    }

    public void R20(Lexer.Token token) {
        String s = "<Print> ::=    put ( <Expression>);";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if(compareLexemes("(")){
            token = R25(manager.getNextToken());
            manager.addToNewArray(token);
        }
        //token = R25(manager.getNextToken());
        //manager.addToNewArray(token);

        if (!token.data.equals(")")) {
            printError(token, ")");
        }
        //compareLexemes(";");
        if(compareLexemes(";")){
            //?
            manager.addToNewArray(token);
        }
    }

    public void R21(Lexer.Token token) {
        String s = "<Scan> ::=    get ( <IDs )";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);

        //compareLexemes("(");
        if(compareLexemes("(")){
            token = R13(manager.getNextToken());
            manager.addToNewArray(token);
        }
        //token = R13(manager.getNextToken());
        //manager.addToNewArray(token);
        if (!token.data.equals(")")) {
            printError(token, ")");
        }

        //if the nextToken and ";" are the same
        if(!compareLexemes(";")){
            return;
        }
    }

    public void R22(Lexer.Token token) {
        String s = "<While> ::=  while ( <Condition>  )  <Statement>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        //compareLexemes("(");
        if(compareLexemes("(")){
            token = R23(token);
            manager.addToNewArray(token);
        }
        //token = R23(token);
        //manager.addToNewArray(token);
        if (!token.data.equals(")")) {
            printError(token, ")");

        }
        R15(manager.getNextToken());
    }

    public Lexer.Token R23(Lexer.Token token) {
        String s = "<Condition> ::=     <Expression>  <Relop>   <Expression>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = R25(manager.getNextToken());
        manager.addToNewArray(token);
        token = R24(token);
        return R25(manager.getNextToken());
    }

    public Lexer.Token R24(Lexer.Token token) {
        String s = "<Relop> ::=        ==   |   !=    |   >     |   <    |  <=   |    =>        ";
        //token.addToRules(s);
        //manager.addToNewArray(token);
        System.out.println("TOKEN: " + token.toString());
        System.out.println("TOKEN: " + token.toString());
        System.out.println("TOKEN: " + token.toString());
        System.out.println("TOKEN: " + token.toString());
        System.out.println("TOKEN: " + token.toString());
        System.out.println(s);
        if (token.data.equals("==") ||
                token.data.equals("!=") ||
                token.data.equals(">") ||
                token.data.equals("<") ||
                token.data.equals(">=") ||
                token.data.equals("<=")){
            token.addToRules(s);
            manager.addToNewArray(token);
            token = manager.getNextToken();
        }
        return token;
    }

    public Lexer.Token R25(Lexer.Token token) {
        String s = "<Expression>  ::=    <Expression> + <Term>    | <Expression>  - <Term>    |    <Term>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        Lexer.Token newToken = R26TERM(token);
        return R25EMPTY(newToken);
    }

    private Lexer.Token R25EMPTY(Lexer.Token token) {
        String s = "<Expression> ::= + <Term> <Expression> | - <Term> <Expression> | <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("+") || token.data.equals("-")) {
            token = R26TERM(manager.getNextToken());
            manager.addToNewArray(token);
            return R25EMPTY(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R26TERM(Lexer.Token token) {
        String s = "<Term>    ::=      <Term>  *  <Factor>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = R27(token);
        manager.addToNewArray(token);
        return R26(token);
    }

    public Lexer.Token R26(Lexer.Token token) {
        String s = "<Term>    ::=      <Term>  *  <Factor>     |   <Term>  /  <Factor>     |     <EMPTY>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("*") || token.data.equals("/")) {
            token = R27(manager.getNextToken());
            manager.addToNewArray(token);
            return R26(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R27(Lexer.Token token) {
        String s = "<Factor> ::=      -  <Primary>    |    <Primary>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.data.equals("-")) {
            token = R28(manager.getNextToken());
        } else {
            token = R28(token);
        }
        manager.addToNewArray(token);
        return token;
    }

    public Lexer.Token R28(Lexer.Token token) {
        String s = "<Primary> ::=     <Identifier>  |  <Integer>  |   <Identifier>  ( <IDs> )   |   ( <Expression> )   |  ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        if (token.type == Lexer.TokenType.IDENTIFIER) {
            token = R28EMPTY(token);
            manager.addToNewArray(token);
            return token;
        } else if (token.data.equals("(")) {
            token = R25(manager.getNextToken());
            manager.addToNewArray(token);
            if (!token.data.equals(")")) {
                printError(token, ")");
            } else {
//                return token;
                return manager.getNextToken();
            }
        } else if (token.data.equals("int")
                || token.data.equals("real")
                || token.data.equals("true")
                || token.data.equals("false")
                || token.type == Lexer.TokenType.NUMBER) {
            token = manager.getNextToken();
            token.addToRules(s);
            manager.addToNewArray(token);
        } else {
            printError(token, "NUMBER");
        }
        return token;
    }

    public Lexer.Token R28EMPTY(Lexer.Token token) {
        String s = "<Primary> ::= ( <IDs> ) | <Empty>";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);

        if (!token.data.equals("(")) {
            return token;
        } else {
            token = R13(manager.getNextToken());
            manager.addToNewArray(token);
        }

        if (!token.data.equals(")")) {
            printError(token, ")");
        } else {
            token = manager.getNextToken();
        }
        return token;
    }

    public void R29(Lexer.Token token) {
        String s = "<Empty>   ::=   ";
        token.addToRules(s);
        manager.addToNewArray(token);

        System.out.println(s);
    }

}