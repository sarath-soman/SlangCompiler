package com.slang.lexer;

import com.slang.ast.Token;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by sarath on 18/3/17.
 */
public class LexerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNum() {
        Lexer lexer = new Lexer("123");
        lexer.eat();
        Token token = lexer.getCurrentToken();
        Assert.assertTrue(Token.NUM.equals(token));
        Assert.assertTrue(123 == lexer.getIntegerNum());

        lexer = new Lexer("123.3456");
        lexer.eat();
        token = lexer.getCurrentToken();
        Assert.assertTrue(Token.NUM.equals(token));
        Assert.assertTrue(123.3456 == lexer.getDoubleNum());

        lexer = new Lexer("123.3456.344");
        exception.expect(RuntimeException.class);
        lexer.eat();


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

    @Test
    public void testExpect() {
        Lexer lexer = new Lexer("(");
        lexer.eat();
        Assert.assertTrue(Token.OPAR.equals(lexer.getCurrentToken()));

        lexer.eat();
        exception.expect(RuntimeException.class);
        lexer.expect(Token.CPAR);

        lexer = new Lexer("()");
        lexer.eat();
        Assert.assertTrue(Token.OPAR.equals(lexer.getCurrentToken()));

        lexer.eat();
        lexer.expect(Token.CPAR);
    }

    @Test
    public void testPRINT_PRINTLN() {
        Lexer lexer = new Lexer("print  ");
        lexer.eat();
        Assert.assertTrue(Token.PRINT.equals(lexer.getCurrentToken()));

        lexer = new Lexer("println  ");
        lexer.eat();
        Assert.assertTrue(Token.PRINTLN.equals(lexer.getCurrentToken()));
    }

    @Test
    public void testStringLiteral() {
        Lexer lexer = new Lexer("\"sdasda\"");
        lexer.eat();
        Assert.assertTrue(Token.STRLTRL.equals(lexer.getCurrentToken()));
        Assert.assertTrue("sdasda".equals(lexer.getStringLiteral()));

        lexer = new Lexer("\"sdasda");
        exception.expect(RuntimeException.class);
        lexer.eat();
        Assert.assertTrue(Token.STRLTRL.equals(lexer.getCurrentToken()));
        Assert.assertTrue("sdasda".equals(lexer.getStringLiteral()));
    }

}
