package com.slang.parser;

import com.slang.ast.*;
import com.slang.lexer.Lexer;

/**
 * Created by sarath on 16/3/17.
 */
public class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Statement parseStatement() {
        lexer.eat();
        Token token = lexer.getCurrentToken();
        if(Token.PRINT == token) {
            Expression expression = parseExpression();
            return new PrintStatement(expression);
        }

        if(Token.PRINTLN == token) {
            Expression expression = parseExpression();
            return new PrintlnStatement(expression);
        }

        throw new RuntimeException("Expected PRINT or PRINTLN");

    }

    public Expression parseExpression() {
        Expression expression = parseTerm();
        Token token = lexer.getCurrentToken();
        while (Token.ADD == token || Token.SUB == token) {
            Expression rightExp = parseTerm();
            expression = new BinaryExpression(expression, rightExp, token);
            token = lexer.getCurrentToken();
        }
        return expression;
    }

    public Expression parseTerm() {
        Expression expression = parseFactor();
        lexer.eat();
        Token token = lexer.getCurrentToken();
        while (Token.MUL == token || Token.DIV == token) {
            Expression rightExp = parseFactor();
            expression = new BinaryExpression(expression, rightExp, token);
            lexer.eat();
            token = lexer.getCurrentToken();
        }
        return expression;
    }

    public Expression parseFactor() {
        lexer.eat();
        Token token = lexer.getCurrentToken();

        switch (token) {
            case NUM:
                return new NumericExpression(lexer.getNum());
            case ADD:
                return parseFactor();
            case SUB:
                Expression leftExp = parseFactor();
                return new UnaryExpression(leftExp,
                        lexer.getPreviousToken());
            case OPAR:
                Expression expression = parseExpression();
                lexer.expect(Token.CPAR);
                return expression;
            case STRLTRL:
                return new StringLiteral(lexer.getStringLiteral());

            default:
                throw new RuntimeException("Un expected token at leaf : " + token);
        }
    }
}
