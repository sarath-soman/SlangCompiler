package com.slang.lexer;

import com.slang.ast.Token;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sarath on 18/3/17.
 */
public class LexerTest {

    @Test
    public void testNum() {
        Lexer lexer = new Lexer("123");
        lexer.eat();
        Token token = lexer.getCurrentToken();
        Assert.assertTrue(Token.NUM.equals(token));
        Assert.assertTrue(123 == lexer.getNum());
    }

    @Test
    public void testADD_SUB_DIV_MUL() {
        Lexer lexer = new Lexer("+ - / *");
        lexer.eat();
        Assert.assertTrue(Token.ADD.equals(lexer.getCurrentToken()));

        lexer.eat();
        Assert.assertTrue(Token.SUB.equals(lexer.getCurrentToken()));

        lexer.eat();
        Assert.assertTrue(Token.DIV.equals(lexer.getCurrentToken()));

        lexer.eat();
        Assert.assertTrue(Token.MUL.equals(lexer.getCurrentToken()));
    }

    @Test
    public void testOPAR_CPAR() {
        Lexer lexer = new Lexer("()");
        lexer.eat();
        Assert.assertTrue(Token.OPAR.equals(lexer.getCurrentToken()));

        lexer.eat();
        Assert.assertTrue(Token.CPAR.equals(lexer.getCurrentToken()));

    }

}
