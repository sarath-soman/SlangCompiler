package com.slang.parser;

import com.slang.ast.Expression;
import com.slang.lexer.Lexer;

/**
 * Created by sarath on 16/3/17.
 */
public class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expression parseExpression() {
        return null;
    }

    public Expression parseTerm() {
        return null;
    }

    public Expression parseFactor() {
        return null;
    }
}
