package com.slang;

import com.slang.ast.Statement;
import com.slang.lexer.Lexer;
import com.slang.parser.Parser;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;
import com.slang.visitor.Interpreter;
import com.slang.visitor.InterpreterContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by sarath on 24/3/17.
 */
public class ProgramTest {

    @Test
    public void testExpEvalOnVariables() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("var x; var y; var z; var a; var b; x = 23; y = 90; z = 6; a = 65; b = 54; var val; val = x/y*z+a-b;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });
        Assert.assertTrue(context.getSymbolInfo("val").getDoubleValue() == 12.533333333333331);
    }

    @Test
    public void testVarDecAndAssign() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("var x = 10;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });
        Assert.assertTrue(context.getSymbolInfo("x").getIntegerValue() == 10);

        lexer = new Lexer("var y = 23/90*6+65-54;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        IVisitor visitor2 = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor2, context);
                });
        Assert.assertTrue(context.getSymbolInfo("y").getDoubleValue() == 12.533333333333331);
    }

    @Test
    public void testImplicitTypes() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("var a = 10; var b = 10.1; var c = 10.34f; var d = 10l; var e = \"asdasda\";");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        IVisitor visitor = new Interpreter();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(visitor, context);
                });

        Assert.assertTrue(Type.INTEGER.equals(context.getSymbolInfo("a").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("a").getIntegerValue() == 10);

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("b").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("b").getDoubleValue() == 10.1);

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("c").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("c").getFloatValue() == 10.34f);

        Assert.assertTrue(Type.LONG.equals(context.getSymbolInfo("d").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("d").getLongValue() == 10l);

        Assert.assertTrue(Type.STRING.equals(context.getSymbolInfo("e").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("e").getStringValue().equals("asdasda"));

    }


    @Test
    public void testTypesPromotedAfterMathOperations() {
        Context context = new InterpreterContext(null);

        //Integer as left operand
        Lexer lexer = new Lexer("var a = 10/10; var b = 10/2.5; var c = 10/2.5f; var d = 100/10l;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("a").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("a").getDoubleValue() == 1.0);

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("b").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("b").getDoubleValue() == 4);

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("c").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("c").getFloatValue() == 4.0f);

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("d").getDataType()));
        Assert.assertTrue(context.getSymbolInfo("d").getDoubleValue() == 10.0);

        //Float as left operand and rest of the data types in right operand
        lexer = new Lexer("var a1 = 14.34f/10; var b1 = 14.34f/2l; var c1 = 14.45f/2.5f; var d1 = 14.45f/2.5;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("a1").getDataType()));

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("b1").getDataType()));

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("c1").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("d1").getDataType()));


        //Long as left operand and rest of the data types in right operand
        lexer = new Lexer("var a2 = 10l/10; var b2 = 10l/2l; var c2 = 10l/2.5f; var d2 = 10l/2.5;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("a2").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("b2").getDataType()));

        Assert.assertTrue(Type.FLOAT.equals(context.getSymbolInfo("c2").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("d2").getDataType()));

        //Double as left operand and rest of the data types in right operand
        lexer = new Lexer("var a3 = 10.1/10; var b3 = 10.1/2l; var c3 = 10.1/2.5f; var d3 = 10.1/2.5;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("a3").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("b3").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("c3").getDataType()));

        Assert.assertTrue(Type.DOUBLE.equals(context.getSymbolInfo("d3").getDataType()));
    }
}
