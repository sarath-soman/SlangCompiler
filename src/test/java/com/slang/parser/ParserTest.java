package com.slang.parser;

import com.slang.ast.*;
import com.slang.lexer.Lexer;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;
import com.slang.visitor.Interpretter;
import com.slang.visitor.InterpretterContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        Context context = new InterpretterContext(null);

        Lexer lexer = new Lexer("20/5 * 2 + 4");
        Parser parser = new Parser(lexer);
        Expression expression = parser.parseExpression();
        IVisitor visitor = new Interpretter();
        Assert.assertTrue(12.0 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("20+3*4-1");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(31 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("20+3/-4-1   ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(18.25 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("20+3/+4-1      ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(19.75 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("2 + (10 / -5) * (-1)   ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(4 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("(10 / -5) * (-1) + 2  ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(4 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("56/43*5+45-67");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(-15.488372093023258 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("23/90*6+65-54");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue(12.533333333333331 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("\"23/90*6+65-54\"");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpretter();
        Assert.assertTrue("23/90*6+65-54".equals(expression.accept(visitor, context).getStringValue()));

    }

    @Test
    public void testPrintAndPrintlnProduction() {
        Context context = new InterpretterContext(null);

        Lexer lexer = new Lexer("print 20 + 30;");
        Parser parser = new Parser(lexer);
        Statement statement = parser.parseStatement();
        IVisitor visitor = new Interpretter();
        statement.accept(visitor, context);

        lexer = new Lexer("println 20 + 30;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpretter();
        statement.accept(visitor, context);

        lexer = new Lexer("println \"Hello, World\";");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpretter();
        statement.accept(visitor, context);

        lexer = new Lexer("println true;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpretter();
        statement.accept(visitor, context);

        lexer = new Lexer("println false;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpretter();
        statement.accept(visitor, context);
    }

    @Test
    public void testParseStatements() {
        Context context = new InterpretterContext(null);

        Lexer lexer = new Lexer("var x; var y;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpretter();
        statements.stream()
                .forEach(statement -> statement.accept(visitor, context));
    }

    @Test
    public void testParseStatements1() {
        Context context = new InterpretterContext(null);

        Lexer lexer = new Lexer("var x; x = 10;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpretter();
        statements.stream()
                .forEach(statement -> {
                    System.out.println(statement);
                    statement.accept(visitor, context);
                });
        System.out.println(context.getSymbolInfo("x"));
    }
}
