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

    @Test
    public void testRelationalExpressionEQ() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("var a = 10; var b = 10/10; var c = 10 == 10;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("a").getIntegerValue() == 10);
        Assert.assertTrue(context.getSymbolInfo("b").getDoubleValue() == 1);
        Assert.assertTrue(context.getSymbolInfo("c").getBoolValue() == true);

        lexer = new Lexer("var exp1 = 10 == 10l; var exp2 = 10 == 10.0; var exp3 = 10 == 10.0f; " +
                "var exp4 = (10 == 10.1); var exp5 = 10 == 11;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("exp1").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp2").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp3").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp4").getBoolValue() == false);
        Assert.assertTrue(context.getSymbolInfo("exp5").getBoolValue() == false);

        lexer = new Lexer("var exp6 = 10l == 10; var exp7 = 10l == 10.0; var exp8 = 10l == 10.0f; " +
                "var exp9 = (10l == 10); var exp10 = 10l == 11; var exp11 = (10l == 11);");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("exp6").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp7").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp8").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp9").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp10").getBoolValue() == false);
        Assert.assertTrue(context.getSymbolInfo("exp11").getBoolValue() == false);


        lexer = new Lexer("var exp12 = 10.0f == 10; var exp13 = 10.0f == 10.0; var exp14 = 10l == 10.0f; " +
                "var exp15 = (10l == 10); var exp16 = 10l == 11; var exp17 = (10l == 11); var exp18 = 10.12 == 10.12; var exp19 = 10.12 == 10.21;");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("exp12").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp13").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp14").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp15").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp16").getBoolValue() == false);
        Assert.assertTrue(context.getSymbolInfo("exp17").getBoolValue() == false);
        Assert.assertTrue(context.getSymbolInfo("exp18").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp19").getBoolValue() == false);

    }

    @Test
    public void testLogicalExpression() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("var c = 10 < 10;");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("c").getBoolValue() == false);

        lexer = new Lexer("var exp6 = true || 10 < 2; var exp7 = (true && false); var exp8 = !false; var exp10 = true; var exp9 = !(true && false || exp10);");
        parser = new Parser(lexer);
        statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
        Assert.assertTrue(context.getSymbolInfo("exp6").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp7").getBoolValue() == false);
        Assert.assertTrue(context.getSymbolInfo("exp8").getBoolValue() == true);
        Assert.assertTrue(context.getSymbolInfo("exp9").getBoolValue() == false);
    }

    @Test
    public void testIfStatement() {
        Context context = new InterpreterContext(null);

        Lexer lexer = new Lexer("if(10 == 10) print(20); endif");
        Parser parser = new Parser(lexer);
        List<Statement> statements = parser.parseStatements();
        statements.stream()
                .forEach(statement -> {
                    statement.accept(new Interpreter(), context);
                });
    }
}
