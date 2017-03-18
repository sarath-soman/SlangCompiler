package com.slang.parser;

import com.slang.ast.BinaryExpression;
import com.slang.ast.Expression;
import com.slang.ast.NumericExpression;
import com.slang.ast.UnaryExpression;
import com.slang.lexer.Lexer;
import com.slang.visitor.IVisitor;
import com.slang.visitor.Interpretter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sarath on 18/3/17.
 */
public class ParserTest {

    @Test
    public void testParseFactor() {
        Lexer lexer = new Lexer("123");
        Parser parser = new Parser(lexer);
        Expression expression = parser.parseExpression();
        Assert.assertTrue(NumericExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(expression).getValue());

        lexer = new Lexer("+ 123");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        Assert.assertTrue(NumericExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(expression).getValue());

        lexer = new Lexer("- 123");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        Assert.assertTrue(UnaryExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(UnaryExpression.class.cast(expression).getLeftExpression()).getValue());
    }

    @Test
    public void testParseExpression() {
        Lexer lexer = new Lexer("20/5 * 2 + 4");
        Parser parser = new Parser(lexer);
        Expression expression = parser.parseExpression();
        IVisitor visitor = new Interpretter();
        Assert.assertTrue(12.0 ==((NumericExpression)expression.accept(visitor)).getValue());
    }
}
