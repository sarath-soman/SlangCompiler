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
        System.out.println(token);
        Assert.assertTrue(Token.NUM.equals(token));
        System.out.println(lexer.getNum());
        Assert.assertTrue(123 == lexer.getNum());
    }

}
