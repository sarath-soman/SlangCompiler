package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.TypeCategory;
import com.slang.ast.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Sarath on 06/04/2017.
 */
public class SemanticAnalyzer implements IVisitor{

    @Override
    public SymbolInfo visit(NumericExpression expression, Context context) {
        return SymbolInfo.builder().withDataType(expression.getDataType()).build();
    }

    @Override
    public SymbolInfo visit(UnaryExpression expression, Context context) {
        SymbolInfo symbolInfo = expression.getLeftExpression().accept(this, context);
        Type dataType = symbolInfo.getDataType();
        if (!(dataType == Type.INTEGER || dataType == Type.LONG || dataType == Type.FLOAT || dataType == Type.DOUBLE)) {
            throw new RuntimeException("Unary operator " + expression.getOperator() + " is not allowed on datatype " + dataType);
        }
        return symbolInfo;
    }

    @Override
    public SymbolInfo visit(ArithmeticExpressionExpression expression, Context context) {
        SymbolInfo lhsInfo = expression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = expression.getRightExpression().accept(this, context);
        return TypeCheckerHelper.checkArithmeticExpresion(lhsInfo, rhsInfo, expression.getOperator());
    }

    @Override
    public SymbolInfo visit(StringLiteral stringLiteral, Context context) {
        return SymbolInfo.builder().withDataType(Type.STRING).build();
    }

    @Override
    public SymbolInfo visit(BooleanExpression booleanExpression, Context context) {
        return SymbolInfo.builder().withDataType(Type.BOOL).build();
    }

    @Override
    public SymbolInfo visit(VariableExpression variableExpression, Context context) {
        return context.getSymbolInfo(variableExpression.getVariableName());
    }

    @Override
    public SymbolInfo visit(RelationalExpression relationalExpression, Context context) {
        SymbolInfo lhsInfo = relationalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = relationalExpression.getRightExpression().accept(this, context);
        return TypeCheckerHelper.checkRelationalExpression(lhsInfo, rhsInfo, relationalExpression.getOperator());
    }

    @Override
    public SymbolInfo visit(LogicalExpression logicalExpression, Context context) {
        SymbolInfo lhsInfo = logicalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = logicalExpression.getRightExpression().accept(this, context);

        Token operator = logicalExpression.getOperator();
        if (lhsInfo.getDataType() == Type.BOOL && rhsInfo.getDataType() == Type.BOOL
                && (operator == Token.OR || operator == Token.ANDAND)){
            return SymbolInfo.builder().withDataType(Type.BOOL).build();
        }
        throw new RuntimeException("Unsupported types lhs : " + lhsInfo.getDataType() + ", rhs : " + rhsInfo.getDataType() + " on operator " + operator);
    }

    @Override
    public SymbolInfo visit(NotExpression notExpression, Context context) {
        SymbolInfo symbolInfo = notExpression.getExpression().accept(this, context);
        if(symbolInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Unsupported getType " + symbolInfo.getDataType() + " on not operator ");
        }
        return SymbolInfo.builder().withDataType(Type.BOOL).build();
    }

    @Override
    public SymbolInfo visit(LambdaExpression lambdaExpression, Context context) {
        //TODO getType check lambda expression
        //TODO tree walk and find the correct variable to capture
        final Function function = lambdaExpression.getFunction().clone();
        function.setCapturedVariables(new LinkedHashMap<>(context.getSymbolTable()));
        function.accept(this, context);

        return SymbolInfo.builder().withDataType(function.getType()).withFunctionValue(function).build();
    }

    @Override
    public SymbolInfo visit(PrintStatement printStatement, Context context) {
        printStatement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public SymbolInfo visit(PrintlnStatement printlnStatement, Context context) {
        printlnStatement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context) {
//        variableDeclarationStatement.getVariableExpression().accept(this, context);
        context.addToSymbolTable(variableDeclarationStatement.getVariableExpression().getVariableName(), new SymbolInfo());
        return null;
    }

    @Override
    public SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context) {
        SymbolInfo variableInfo = context.getSymbolInfo(variableAssignmentStatement.getVariableName());
        SymbolInfo rhsExpInfo = variableAssignmentStatement.getExpression().accept(this, context);

        if(null == variableInfo) {
            throw new RuntimeException("Cannot assign to an undefined getType");
        } else if(null == variableInfo.getDataType()) {
            context.addToSymbolTable(variableAssignmentStatement.getVariableName(), rhsExpInfo);
//            System.out.println(rhsExpInfo);
//            if(rhsExpInfo.getDataType().getTypeCategory() == TypeCategory.FUNCTION) {
//                variableInfo.setFunctionValue(rhsExpInfo.getFunctionValue());
//            }
//            variableInfo.setDataType(rhsExpInfo.getDataType());
            return null;
        } else if(variableInfo.getDataType() != rhsExpInfo.getDataType()) {
            throw new RuntimeException("Variable getType (" + variableInfo.getDataType() + ") doesn't match the rhs exp getType(" + rhsExpInfo.getDataType() + ")");
        }

        return null;

    }

    @Override
    public SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context) {
        variableDeclAndAssignStatement.getVariableDeclarationStatement().accept(this, context);
        return variableDeclAndAssignStatement.getVariableAssignmentStatement().accept(this, context);
    }

    @Override
    public SymbolInfo visit(IfStatement ifStatement, Context context) {
        SymbolInfo ifConditionInfo = ifStatement.getBooleanExpression().accept(this, context);
        if(ifConditionInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Conditional expressions in if should be of getType boolean");
        }

        Context ifContext = new LexicalContext(context);
        ifContext.setCurrentBlock(ifStatement);

        if(null != ifStatement.getTrueBody()) {
            for(Statement statement : ifStatement.getTrueBody()) {
                if(BreakStatement.class.isAssignableFrom(statement.getClass()) && !context.isInLoopBlock()) {
                    throw new RuntimeException("Break statement is not allowed without a parent loop");
                } else if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                    ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                    SymbolInfo returnInfo = returnStatement.accept(this, context);
                    Function currentFunction = context.getCurrentFunction();
                    if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                        throw new RuntimeException("Return getType doesn't (" + currentFunction.getReturnType() + ") match function return getType ( " + returnInfo.getDataType() + ")");
                    }
                    //TODO remove unused code - rewrite AST
                    continue;
                }
                statement.accept(this, ifContext);
            }
        }

        if(null != ifStatement.getFalseBody()) {
            for(Statement statement : ifStatement.getFalseBody()) {
                if(BreakStatement.class.isAssignableFrom(statement.getClass()) && !context.isInLoopBlock()) {
                    throw new RuntimeException("Break statement is not allowed without a parent loop");
                } else if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                    ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                    SymbolInfo returnInfo = returnStatement.accept(this, ifContext);
                    Function currentFunction = context.getCurrentFunction();
                    if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                        throw new RuntimeException("Return getType doesn't (" + currentFunction.getReturnType() + ") match function return getType ( " + returnInfo.getDataType() + ")");
                    }
                    //TODO remove unused code - rewrite AST
                    continue;
                }
                statement.accept(this, ifContext);
            }
        }
        return null;
    }

    @Override
    public SymbolInfo visit(WhileStatement whileStatement, Context context) {
        SymbolInfo conditionInfo = whileStatement.getExpression().accept(this, context);
        if (conditionInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Conditional expressions in while should be of getType boolean");
        }

        for(Statement statement : whileStatement.getBody()) {
            Context whileContext = new LexicalContext(context);
            whileContext.setCurrentBlock(whileStatement);
            if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                SymbolInfo returnInfo = returnStatement.accept(this, whileContext);
                Function currentFunction = context.getCurrentFunction();
                if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                    throw new RuntimeException("Return getType doesn't (" + currentFunction.getReturnType() + ") match function return getType ( " + returnInfo.getDataType() + ")");
                }
                //TODO remove unused code - rewrite AST
                continue;
            }
            statement.accept(this, whileContext);
        }

        return null;
    }

    @Override
    public SymbolInfo visit(BreakStatement breakStatement, Context context) {
        return null;
    }

    @Override
    public SymbolInfo visit(Function function, Context context) {
        Context functionContext = new LexicalContext(context.getFunctionTable());

        functionContext.setCurrentFunction(function);

        if (null != function.getCapturedVariables()) {
            function.getCapturedVariables().entrySet().forEach(capturedEntry ->
                    functionContext.addToSymbolTable(capturedEntry.getKey(),
                            SymbolInfo.builder()
                                    .withDataType(capturedEntry.getValue().getDataType())
                                    .build()));
        }
        function.getFormalArguments().entrySet().forEach(formalParamEntry ->
                functionContext.addToSymbolTable(formalParamEntry.getKey(),
                        SymbolInfo.builder()
                                .withDataType(formalParamEntry.getValue())
                                .build()));

        for(Statement statement : function.getBody()) {
            if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                SymbolInfo returnInfo = returnStatement.accept(this, functionContext);
                Function currentFunction = functionContext.getCurrentFunction();
                if(TypeCheckerHelper.isNotEqual(currentFunction.getReturnType(),returnInfo.getDataType())) {
                    throw new RuntimeException("Return getType doesn't (" + currentFunction.getReturnType() + ") match function return getType ( " + returnInfo.getDataType() + ")");
                }
                //TODO remove unused code - rewrite AST
                continue;
            }
            statement.accept(this, functionContext);
        }
        return null;
    }

    @Override
    public SymbolInfo visit(ReturnStatement returnStatement, Context context) {
        return returnStatement.getExpression().accept(this, context);
    }

    @Override
    public SymbolInfo visit(FunctionInvokeExpression functionInvokeExpression, Context context) {
        Function function = context.getFunction(functionInvokeExpression.getFunctionName());

        if(null == function) {
            SymbolInfo functionValue = context.getSymbolInfo(functionInvokeExpression.getFunctionName());
            if(null == functionValue || functionValue.getDataType().getTypeCategory() != TypeCategory.FUNCTION) {
                throw new RuntimeException("Undefined function ex " + functionInvokeExpression.getFunctionName());
            }

            function = functionValue.getFunctionValue();
        }

        if(null == function) {
            throw new RuntimeException("Undefined function " + functionInvokeExpression.getFunctionName());
        }

        if(function.getFormalArguments().entrySet().size() != functionInvokeExpression.getActualFunctionArguments().size()) {
            throw new RuntimeException("Formal and actual param size doesn't match");
        }

        Iterator<Map.Entry<String, Type>> formalParamEntryIterator = function.getFormalArguments().entrySet().iterator();
        for(Expression expression : functionInvokeExpression.getActualFunctionArguments()) {
            SymbolInfo expressionInfo = expression.accept(this, context);
            Type formalParamtype = formalParamEntryIterator.next().getValue();
            if(formalParamtype != expressionInfo.getDataType()) {
                throw new RuntimeException("Formal and actual param getType mismatch");
            }

        }
        return SymbolInfo.builder().withDataType(function.getReturnType()).build();
    }

    @Override
    public SymbolInfo visit(FunctionInvokeStatement functionInvokeStatement, Context context) {
        return functionInvokeStatement.getFunctionInvokeExpression().accept(this, context);
    }

    @Override
    public SymbolInfo visit(VoidExpression voidExpression, Context context) {
        return SymbolInfo.builder().withDataType(Type.VOID).build();
    }

    @Override
    public SymbolInfo visit(Module module, Context context) {
        Context moduleContext = new LexicalContext(module.getFunctionsMap());
        module.getFunctionsMap().entrySet().stream().forEach(stringFunctionEntry -> {
            stringFunctionEntry.getValue().accept(this, moduleContext);
        });
        return null;
    }
}
