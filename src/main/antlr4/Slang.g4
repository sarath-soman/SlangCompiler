// Slang ANTLR4 grammar generated to match com.slang.lexer.Lexer and com.slang.parser.Parser
// Target: Java (default)

grammar Slang;

// ---------------
// Parser rules
// ---------------

program
    : functionDecl* EOF
    ;

functionDecl
    : FUNCTION type IDENTIFIER OPAR formalParamList? CPAR statement* END
    ;

formalParamList
    : formalParam (COMMA formalParam)*
    ;

formalParam
    : type IDENTIFIER
    ;

// Types, including function types: (T1, T2, ...) -> R
type
    : VOID
    | INT_T
    | LONG_T
    | FLOAT_T
    | DOUBLE_T
    | BOOLEAN_T
    | STRING_T
    | OPAR typeList? CPAR ARROW type
    ;

typeList
    : type (COMMA type)*
    ;

// ----------------
// Statements
// ----------------

statement
    : printStmt
    | printlnStmt
    | varDecl
    | varDeclAssignLambda
    | varDeclAssign
    | assignLambda
    | assignCall
    | assignExpr
    | ifStmt
    | whileStmt
    | breakStmt
    | returnStmt
    | functionCallStmt
    ;

printStmt
    : PRINT expression SEMI
    ;

printlnStmt
    : PRINTLN expression SEMI
    ;

varDecl
    : VAR IDENTIFIER SEMI
    ;

varDeclAssign
    : VAR IDENTIFIER EQ (functionCall | expression) SEMI
    ;

varDeclAssignLambda
    : VAR IDENTIFIER EQ lambdaExpr
    ;

assignExpr
    : IDENTIFIER EQ expression SEMI
    ;

assignCall
    : IDENTIFIER EQ functionCall SEMI
    ;

assignLambda
    : IDENTIFIER EQ lambdaExpr
    ;

lambdaExpr
    : LAMBDA type OPAR formalParamList? CPAR statement* ENDLAMBDA
    ;

functionCallStmt
    : functionCall SEMI
    ;

functionCall
    : IDENTIFIER OPAR argList? CPAR
    ;

argList
    : expression (COMMA expression)*
    ;

ifStmt
    : IF expression THEN
      statement+ (ELSE statement+)?
      ENDIF
    ;

whileStmt
    : WHILE expression
      statement+
      WEND
    ;

breakStmt
    : BREAK SEMI
    ;

returnStmt
    : RETURN SEMI
    | RETURN expression SEMI
    | RETURN functionCall SEMI
    ;

// ----------------
// Expressions (precedence: unary > * / > + - > relational > && ||)
// ----------------

expression
    : logicalOrExpr
    ;

logicalOrExpr
    : logicalAndExpr (OROR logicalAndExpr)*
    ;

logicalAndExpr
    : relationalExpr (ANDAND relationalExpr)*
    ;

relationalExpr
    : additiveExpr ((DEQ | LT | LTE | GT | GTE) additiveExpr)*
    ;

additiveExpr
    : multiplicativeExpr ((ADD | SUB) multiplicativeExpr)*
    ;

multiplicativeExpr
    : unaryExpr ((MUL | DIV) unaryExpr)*
    ;

unaryExpr
    : NOT unaryExpr
    | ADD unaryExpr
    | SUB unaryExpr
    | primary
    ;

primary
    : INT_LIT
    | LONG_LIT
    | FLOAT_LIT
    | DOUBLE_LIT
    | STRING_LITERAL
    | TRUE
    | FALSE
    | IDENTIFIER
    | OPAR expression CPAR
    ;

// ---------------
// Lexer rules
// ---------------

FUNCTION   : 'function' ;
RETURN     : 'return' ;
VAR        : 'var' ;
IF         : 'if' ;
THEN       : 'then' ;
ELSE       : 'else' ;
ENDIF      : 'endif' ;
WHILE      : 'while' ;
WEND       : 'wend' ;
BREAK      : 'break' ;
PRINT      : 'print' ;
PRINTLN    : 'println' ;
VOID       : 'void' ;
INT_T      : 'int' ;
LONG_T     : 'long' ;
FLOAT_T    : 'float' ;
DOUBLE_T   : 'double' ;
BOOLEAN_T  : 'boolean' ;
STRING_T   : 'string' ;
END        : 'end' ;
LAMBDA     : 'lambda' ;
ENDLAMBDA  : 'endlambda' ;
TRUE       : 'true' ;
FALSE      : 'false' ;

ARROW      : '->' ;
ANDAND     : '&&' ;
OROR       : '||' ;
DEQ        : '==' ;
LTE        : '<=' ;
GTE        : '>=' ;
LT         : '<' ;
GT         : '>' ;
NOT        : '!' ;
ADD        : '+' ;
SUB        : '-' ;
MUL        : '*' ;
DIV        : '/' ;
EQ         : '=' ;
COMMA      : ',' ;
SEMI       : ';' ;
OPAR       : '(' ;
CPAR       : ')' ;

// Literals
FLOAT_LIT  : DIGITS '.' DIGITS 'f' ;
DOUBLE_LIT : DIGITS '.' DIGITS ;
LONG_LIT   : DIGITS 'l' ;
INT_LIT    : DIGITS ;

STRING_LITERAL
           : '"' (~["\r\n])* '"'
           ;

IDENTIFIER : LETTER (LETTER | DIGIT)* ;

fragment LETTER : [A-Za-z] ;
fragment DIGIT  : [0-9] ;
fragment DIGITS : DIGIT+ ;

WS         : [ \t\r\n]+ -> skip ;


