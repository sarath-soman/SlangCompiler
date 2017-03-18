package com.slang.parser;

import com.slang.ast.Expression;
import com.slang.ast.NumericExpression;
import com.slang.ast.Token;
import com.slang.ast.UnaryExpression;
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
        return parseTerm();
    }

    public Expression parseTerm() {
        return parseFactor();
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
                lexer.eat();
                lexer.expect(Token.CPAR);
                return expression;

            default:
                throw new RuntimeException("Un expected token at leaf : " + token);
        }
    }
}
