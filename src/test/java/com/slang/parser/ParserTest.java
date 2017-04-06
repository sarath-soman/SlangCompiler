package com.slang.parser;

import com.slang.Type;
import com.slang.ast.*;
import com.slang.lexer.Lexer;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;
import com.slang.visitor.Interpreter;
import com.slang.visitor.InterpreterContext;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

/**
 * Created by sarath on 18/3/17.
 */
public class ParserTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testParseFactor() {
        Lexer lexer = new Lexer("123");
        Parser parser = new Parser(lexer);
        Expression expression = parser.parseExpression();
        Assert.assertTrue(NumericExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(expression).getIntegerValue());

        lexer = new Lexer("+ 123");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        Assert.assertTrue(NumericExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(expression).getIntegerValue());

        lexer = new Lexer("- 123");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        Assert.assertTrue(UnaryExpression.class.isInstance(expression));
        Assert.assertTrue(123 == NumericExpression.class.cast(UnaryExpression.class.cast(expression).getLeftExpression()).getIntegerValue());
    }

    @Test
    public void testParseExpression() {
        Context context = new InterpreterContext();

        Lexer lexer = new Lexer("20/5 * 2 + 4");
        Parser parser = new Parser(lexer);
        Expression expression = parser.parseExpression();
        IVisitor visitor = new Interpreter();
        Assert.assertTrue(12 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("20+3*4-1");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(31 ==expression.accept(visitor, context).getIntegerValue());

        lexer = new Lexer("20+3/-4-1   ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(18.25 == expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("20+3/+4-1      ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(19.75 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("2 + (10 / -5) * (-1)   ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(4 == expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("(10 / -5) * (-1) + 2  ");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(4 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("56/43*5+45-67");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(-15.488372093023258 == expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("23/90*6+65-54");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue(12.533333333333331 ==expression.accept(visitor, context).getDoubleValue());

        lexer = new Lexer("\"23/90*6+65-54\"");
        parser = new Parser(lexer);
        expression = parser.parseExpression();
        visitor = new Interpreter();
        Assert.assertTrue("23/90*6+65-54".equals(expression.accept(visitor, context).getStringValue()));

    }

    @Test
    public void testPrintAndPrintlnProduction() {
        Context context = new InterpreterContext();

        Lexer lexer = new Lexer("print 20 + 30;");
        Parser parser = new Parser(lexer);
        Statement statement = parser.parseStatement();
        IVisitor visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("println 20 + 30;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("println \"Hello, World\";");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("println true;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("println false;");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("println(10);");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

        lexer = new Lexer("print(10);");
        parser = new Parser(lexer);
        statement = parser.parseStatement();
        visitor = new Interpreter();
        statement.accept(visitor, context);

    }

    @Test
    public void testParseStatements() {
        Context context = new InterpreterContext();

        Lexer lexer = new Lexer("var x; var y;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> statement.accept(visitor, context));
    }

    @Test
    public void testParseStatements1() {
        Context context = new InterpreterContext();

        Lexer lexer = new Lexer("var x; x = 10;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });
    }

    @Test
    public void testParseStatements2() {
        Context context = new InterpreterContext();

        Lexer lexer = new Lexer("var x; x = 10; var y; y = x / (2 * x) - 100 ;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });

        lexer = new Lexer("var z; z = 100; println z;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });

        lexer = new Lexer("var str1; var val; val = 10; str1 = \"sadasd\"; str1 = val;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        exception.expect(RuntimeException.class);
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });
    }

}
