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

    //
    //R1() needs to first check for a function declaration THEN after we check for function declarations we must check for
    // the dollar signs $$
    //
    public void R1() {
        String s = "Rat20F>  ::=   <Opt Function Definitions>   $$  <Opt Declaration List>  <Statement List>  $$";
        System.out.println(s);
//        manager.currentIndex = 0;
        Lexer.Token token = manager.getCurrentToken(); // we are gettubg tge very first token which should be  KEYWORD: function
//        System.out.println("TOKEN: " + token.toString());
        token = R2(token);
//        if (!token.data.equals("$$")) {
//            printError(token, "$$");
//        }

        //token = R10(token);
        //token = R14(token);
//        if (!token.data.equals("$$")) {
//            printError(token, "$$");
//        }
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
        updateAndPrint(token, s);

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
        updateAndPrint(token, s);


        R4(token);
        return R4EMPTY(token);
    }

    public void R4(Lexer.Token token) {
        System.out.println("Inside R4()");//added  by kevin for printing/testing
        String s = "Function> ::= function  <Identifier>   ( <Opt Parameter List> )  <Opt Declaration List>  <Body>";
        updateAndPrint(token, s);
        token = R13(token);
        if (!token.data.equals("(")) {
            System.out.println("ERROR IS FOUND IN R4()");
            printError(token, "(");
        }

        token = R5(token);
        if (!token.data.equals(")")) {
            System.out.println("ERROR IS FOUND IN R4()");

            printError(token, ")");
        }
        token = R10(token);
        R9(token);
    }

    public Lexer.Token R4EMPTY(Lexer.Token token) {
        String s = "<Function Definitions>' ::= <Function Definitions> | <Empty>";
        updateAndPrint(token, s);
        token = manager.getNextToken();
        if (token.type == Lexer.TokenType.KEYWORD) {
            return token;
        } else {
            return R3(token);
        }
    }

    public Lexer.Token R5(Lexer.Token token) {
        String s = "Opt Parameter List> ::=  <Parameter List>    |     <Empty>";
        updateAndPrint(token, s);
        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
//        manager.tempNextToken(-1);
//        manager.tempNextToken(0);
//        manager.tempNextToken(1);


        if (token.type != Lexer.TokenType.IDENTIFIER) {
            return token;
        }
        token = R6(token);
        manager.addToNewArray(token);
        return token;
    }

    public Lexer.Token R6(Lexer.Token token) {
        String s = "Parameter List>  ::=  <Parameter>    |     <Parameter> , <Parameter List>";
        updateAndPrint(token, s);
        R7(token);
//        return R5(manager.getNextToken());
        return R5(token);//added by kevin for testing
    }

    public void R7(Lexer.Token token) {
        String s = "Parameter> ::=  <IDs >  <Qualifier> ";
        updateAndPrint(token, s);
        token = R13(token);
        R8(token);

    }

    public void R8(Lexer.Token token) {
        String s = "Qualifier> ::= int     |    boolean    |  real ";
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);
        if (!token.data.equals("{")) {
            printError(token, "{");
        }
        // we start off here, our current token is " { "
        token = manager.getNextToken(); // now the token is "return"
        token = R14(token); //we are going ro r13
        if (!token.data.equals("}")) {
            System.out.println("THE ERROR IS HERE");
            printError(token, "}");
        }
    }

    public Lexer.Token R10(Lexer.Token token) {
        String s = "<Opt Declaration List> ::= <Declaration List> | <Empty>";
        updateAndPrint(token, s);
        token = manager.getNextToken();
        if (token.data.equals("int") || token.data.equals("boolean") || token.data.equals("real")) {
            return R11(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R11(Lexer.Token token) {
        String s = "<Declaration List>  := <Declaration> ;     |      <Declaration> ; <Declaration List>";
        updateAndPrint(token, s);
        token = R12(token);
        if (!token.data.equals(";")) {
            System.out.println("ERROR IS HERE IN R11()");
            printError(token, ";nancypelosi");
        }
        return R11Empty(manager.getNextToken());
    }

    public Lexer.Token R11Empty(Lexer.Token token) {
        String s = "<Declaration List> ::= <Declaration List>  |  <Empty>";
        updateAndPrint(token, s);
        if (token.data.equals("int") || token.data.equals("boolean") || token.data.equals("real")) {
            return R11(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R12(Lexer.Token token) {
        String s = "<Declaration> ::=   <Qualifier > <IDs> ";
        updateAndPrint(token, s);
        R8(token);
        token = manager.getNextToken();
        return R13(token);
    }

    public Lexer.Token R13(Lexer.Token token) {
        String s = "<IDs> ::=     <Identifier>    | <Identifier>, <IDs>";
        updateAndPrint(token, s);
        if (!(token.type.equals(Lexer.TokenType.IDENTIFIER))) {
            System.out.println("ERROR IN R13");
            printError(token, "identifier");
//            manager.tempNextToken(-1);
//            manager.tempNextToken(-2);
        }
        token = R13Empty(token);
        manager.addToNewArray(token);
        return token;

    }

    private Lexer.Token R13Empty(Lexer.Token token) {
        String s = "<IDs> ::=     <Identifier>    | <Identifier>, <Empty>";
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);
        //now since we are in the statement list funciton, lets jump ro r15, our token is still " return "
        R15(token);
        return R14Empty(manager.getNextToken());
    }

    private Lexer.Token R14Empty(Lexer.Token token) {
        String s = "<Statement List> ::= <Statement List>  |  <Empty>";
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);
        if (token.data.equals("{")) { //<--- tbh that seems redundant now lmao
            R16(token);
        } else if (token.type == Lexer.TokenType.IDENTIFIER) {
            R17(token);
        } else if (token.data.equals("if")) {
            R18(token);
        } else if (token.data.equals("return")) {    //this is the condition that will be triggered
            R19(token);          //now lets hop to r19
        } else if (token.data.equals("put")) {
            R20(token);
        } else if (token.data.equals("get")) {
            R21(token);
        } else if (token.data.equals("while")) {
            R22(token);
        } else {
            System.out.println("The error is being printed from r15");
            manager.tempNextToken(-1);
            printError(token, "keyword or an identifier");
            manager.tempNextToken(1);
        }
    }

    public void R16(Lexer.Token token) {
        String s = "<Compound> ::=   {  <Statement List>  } ";
        updateAndPrint(token, s);

        token = R14(manager.getNextToken());
        manager.addToNewArray(token);
        if (!token.data.equals("}")) {
            printError(token, "}");
        }
    }

    public void R17(Lexer.Token token) {
        String s = "<Assign> ::=     <Identifier> = <Expression> ;";
        updateAndPrint(token, s);
//        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
        token = manager.getNextToken();
        if (!token.data.equals("=")) {
            manager.tempNextToken(-1);
            printError(token, "=");
            manager.tempNextToken(1);
        }
        token = R25(manager.getNextToken());
        manager.addToNewArray(token);
        if (!token.data.equals(";")) {
            System.out.println("ERROR is HERE IN R17()");
            printError(token, ";lol");
        }
    }

    public void R18(Lexer.Token token) {
        String s = "<If> ::=     if  ( <Condition>  ) <Statement>   IF";
        updateAndPrint(token, s);

        //compareLexemes("(");
        if(compareLexemes("(")){
            System.out.println("INSIDE R18");
            manager.tempNextToken(0);
            R23(token);
        }
//        R23(token);
        //compareLexemes(")");
        if(compareLexemes(")")){
            System.out.println("INSIDE R18");
            manager.tempNextToken(0);
            manager.tempNextToken(1);
//            R15(manager.getNextToken()); //ORIGINAL CODE COMMENTED OUT BY KEVIN
            R15(token); //NEW CODE FOR TESTING BY KEVIN
            R18FI(manager.getNextToken());
        }
        //R15(manager.getNextToken());
        //R18FI(manager.getNextToken());
    }

    public void R18FI(Lexer.Token token) {
        String s = "if  ( <Condition>  ) <Statement>   else  <Statement>  fi ";
//        token.addToRules(s);
//        manager.addToNewArray(token);
//        System.out.println(s);
//
        updateAndPrint(token,s);
        if (token.data.equals("fi")) {
//            R18FI(manager.getNextToken());
        } else if (token.data.equals("else")) {
            R15(manager.getNextToken());
            compareLexemes("fi");
        }

    }

    private boolean compareLexemes(String data) {
        Lexer.Token comparer = manager.getNextToken();
        manager.currentIndex--;
        if (!comparer.data.equals(data)) {
//            printError(comparer, data);
            return false;
        }
        return true;
    }

    public void R19(Lexer.Token token) {       //our token is still "return"
        String s = "<Return> ::=  return ; |  return <Expression> ;";
        updateAndPrint(token,s);
         //we just had the token "return" so we get the next one

        if(!compareLexemes(";Benis")){ // it is not " return ;  then it is an expression
            token = manager.getNextToken();// now our token is  " 5 "
            token = R25(token);
            //when we compare we see that the next token after "return" is  " 5 "
            // now lets hop into r25 since it is NOT " ; "
        }

        System.out.println(s);
        R19_SEMICOLON(token);
    }


    private void R19_SEMICOLON(Lexer.Token token) {
        String s = "<Return>' ::= ; | <Expression>;";
        updateAndPrint(token, s);
        token = manager.getNextToken();
        token.addToRules(s);
        manager.addToNewArray(token);
        if (token.data.equals(";test")) {
            return;
       }
        //else {
//            token = R25(token);
//            manager.addToNewArray(token);
//        }
        System.out.println("printing from r19semicolon");



        printError(token, ";esketit");
        token = R25(token);
        manager.addToNewArray(token);
    }

    public void R20(Lexer.Token token) {
        String s = "<Print> ::=    put ( <Expression>);";
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);

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
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);
        token = R25(manager.getNextToken());
        manager.addToNewArray(token);
        token = R24(token);
        return R25(token);
    }

    public Lexer.Token R24(Lexer.Token token) {
        String s = "<Relop> ::=        ==   |   !=    |   >     |   <    |  <=   |    =>        ";
        updateAndPrint(token,s);
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

    public Lexer.Token R25(Lexer.Token token) {     //if it wasnt a semicolon, it was an expression
        String s = "<Expression>  ::=    <Expression> + <Term>    | <Expression>  - <Term>    |    <Term>";
        updateAndPrint(token, s);
        //our token is 5, so we should get the next token to see if it is a + or a -
        token = manager.getNextToken();

        if (token.data.equals("+") || token.data.equals("-") )
        {
            token = manager.getNextToken();
            token = R26TERM(token);
            manager.addToNewArray(token);
            return R25EMPTY(token);
        }                                   //I think this is af far as i got while debugging actually
        //it will ignore this condition
        //if it is NOT + or -, for example it is * or /

//        else if(token.data.equals("*") || token.data.equals("/") ){
//            then it is a term
//            token = R26()
//        }
        else { //if it is not + or - then it is * or / so call r26
            System.out.println("ELSE STATEMENT BECAUSE IT WAS NOT + OR -");
            token = R26(token);    //token went from 5 to *, now pass that into r26
        }





//        token = manager.getNextToken(); //added by kevin TESTING
//        token = R26TERM(token);//ORIGINAL LIubNE COMMENTED OUT BY KEVIN
//        Lexer.Token newToken = R26(token); //ADDED BY KEVIN FOR TESTING
        return R25EMPTY(token);
    }


    private Lexer.Token R25EMPTY(Lexer.Token token) {
        String s = "<Expression> ::= + <Term> <Expression> | - <Term> <Expression> | <Empty>";
        updateAndPrint(token, s);
        //mayube add this? || token.data.equals("*") ||token.data.equals("/")
        if (token.data.equals("+") || token.data.equals("-")  ) {
            token = manager.getNextToken();
            token = R26TERM(token);
            manager.addToNewArray(token);
            return R25EMPTY(token);
        } else {
            return token;
        }
    }


    public Lexer.Token R26(Lexer.Token token) {
        String s = "<Term>    ::=      <Term>  *  <Factor>     |   <Term>  /  <Factor>     |     <EMPTY>";
        updateAndPrint(token, s);

        //current token is *
        if (token.data.equals("*") || token.data.equals("/")) {
            System.out.println("IT WAS NOT A + OR A -");
            token = R27(manager.getNextToken()); //since we had a * or a /, our next token is a factor of it
            manager.addToNewArray(token);     //im going to run it and see
            return R26(token);
        } else {
            return token;
        }
    }

    public Lexer.Token R26TERM(Lexer.Token token) {
        String s = "<Term>    ::=      <Term>  *  <Factor>";
        updateAndPrint(token, s);
        token = R27(token);
        manager.addToNewArray(token);
        return R26(token);
    }

    public Lexer.Token R27(Lexer.Token token) {
        String s = "<Factor> ::=      -  <Primary>    |    <Primary>"; //it ccan be positive or negatiev
        updateAndPrint(token, s);
        //we just passed in the next token which is " ( " , we can just go the next function from here
        R28(token);//we did not go to the next token because r28 checks the current token
        //check for a PRIMARY

        //we are just checking if our factor is positve or negative
        //first wWTF IS PRIMARY





//        if (token.data.equals("-"))
//        {
//            token = R28(manager.getNextToken());
//        }
//        else
//            {
//            //token = R28(token); //ORIGINAL LINE COMMENTED OUT BY KEVIN 12/3
//            token = R28(manager.getNextToken()); //ADDED BY KEVIN FOR TESTING
//            //12/3 the above line helped us go from 16 errors to 8. I wondered why we
//        }
        manager.addToNewArray(token);
        return token;
    }

    public Lexer.Token R28(Lexer.Token token) {
        String s = "<Primary> ::=     <Identifier>  |  <Integer>  |   <Identifier>  ( <IDs> )   |   ( <Expression> )   |  ";
        updateAndPrint(token, s);
        if (token.type == Lexer.TokenType.IDENTIFIER)
        {
            token = R28EMPTY(token);
            manager.addToNewArray(token);
            return token;

            //12-3: Sahil
//            if(token.data.equals("("))
//            {
//                R13(token);
//            }
        }
        else if (token.data.equals("("))
        {
            System.out.println("R28 PARENTHESIS LOLOOL");
            token = R25(manager.getNextToken()); // goes from ( to fahr, passes fahr to r25
//            token = R13(manager.getNextToken()); // goes from ( to fahr, passes fahr to r25
            //what do we do here
            manager.addToNewArray(token);
            if (!token.data.equals(")"))
            {
                System.out.println("r28 elif");
                manager.tempNextToken(-1);
                printError(token, ")");
                manager.tempNextToken(-1);
            }
            else
                {
//                return token;
                return manager.getNextToken();
                }
        }
        else if (

                token.data.equals("int")
                        || token.data.equals("real")
                        || token.data.equals("true")
                        || token.data.equals("false")
                        || token.type == Lexer.TokenType.NUMBER)
        {
            token = manager.getNextToken();
            token.addToRules(s);
            manager.addToNewArray(token);
        }
        else
            {
            System.out.println("THE ERROR IS IN R28");
            manager.tempNextToken(-1);
            printError(token, "NUMBER");
            manager.tempNextToken(1);
            manager.tempNextToken(1);
            }
        return token;
    }

    public Lexer.Token R28EMPTY(Lexer.Token token) {
        String s = "<Primary> ::= ( <IDs> ) | <Empty>";
        updateAndPrint(token, s);
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
        updateAndPrint(token, s);
    }

    private void updateAndPrint(Lexer.Token token, String s) {
        token.addToRules(s);
        manager.addToNewArray(token);
        System.out.println(s);
    }

}