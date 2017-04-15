package com.slang.ast;

/**
 * Created by sarath on 16/3/17.
 */
public enum Token {

    ADD,
    SUB,
    DIV,
    MUL,
    NUM,

    OPAR,
    CPAR,

    PRINT,
    PRINTLN,
    SEMICLN,

    STRLTRL,

    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOL,
    STRING,
    VAR,

    TRUE,
    FALSE,

    VAR_NAME,

    UNKNOWN,

    EQ,

    DEQ,
    NEQ,
    LT,
    LTE,
    GT,
    GTE,

    ANDAND,
    OR,
    NOT,
    IF,
    ENDIF,
    THEN,
    ELSE,
    WHILE,
    WEND,

    VOID,
    FUNCTION,
    RETURN,
    BREAK,
    COMMA,
    LAMBDA,
    ENDLAMBDA,
    END,
}
