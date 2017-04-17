package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpreter implements IVisitor {

    public SymbolInfo visit(NumericExpression expression, Context context) {
        if (Type.DOUBLE.equals(expression.getDataType())) {
            return new SymbolInfo(expression.getDoubleValue());
        } else if (Type.FLOAT.equals(expression.getDataType())) {
            return new SymbolInfo(expression.getFloatValue());
        } else if (Type.LONG.equals(expression.getDataType())) {
            return new SymbolInfo(expression.getLongValue());
        } else if (Type.INTEGER.equals(expression.getDataType())) {
            return new SymbolInfo(expression.getIntegerValue());
        } else {
            throw new RuntimeException("Unsupported data type");
        }
    }

    public SymbolInfo visit(UnaryExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        if(Token.SUB != expression.getOperator()) {
            if (!(Type.DOUBLE.equals(leftExpVal.getDataType()) || Type.FLOAT.equals(leftExpVal.getDataType())
                    || Type.LONG.equals(leftExpVal.getDataType()) || Type.INTEGER.equals(leftExpVal.getDataType()))) {
                throw new RuntimeException("Unsupported data type : " + leftExpVal.getDataType());
            }
            return leftExpVal;
        }

        //When oper is SUB
        if(Type.DOUBLE.equals(leftExpVal.getDataType())) {
            return new SymbolInfo(leftExpVal.getDoubleValue() * -1);
        } else if(Type.FLOAT.equals(leftExpVal.getDataType())) {
            return new SymbolInfo(leftExpVal.getFloatValue() * -1);
        } else if(Type.LONG.equals(leftExpVal.getDataType())) {
            return new SymbolInfo(leftExpVal.getLongValue() * -1);
        } else if(Type.INTEGER.equals(leftExpVal.getDataType())) {
            return new SymbolInfo(leftExpVal.getIntegerValue() * -1);
        } else {
            throw new RuntimeException("Unsupported data type : " + leftExpVal.getDataType());
        }
    }

    public SymbolInfo visit(ArithmeticExpressionExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        SymbolInfo rightExpVal = expression.getRightExpression().accept(this, context);
        Token token = expression.getOperator();
        SymbolInfo resp = typeCheckAndApplyArithmeticOperator(leftExpVal, rightExpVal, token);
        return resp;
    }

    public SymbolInfo visit(StringLiteral stringLiteral, Context context) {
        return new SymbolInfo(stringLiteral.getStringLiteral());
    }

    public SymbolInfo visit(BooleanExpression booleanExpression, Context context) {
        return new SymbolInfo(booleanExpression.getValue());
    }

    public SymbolInfo visit(VariableExpression variableExpression, Context context) {
        return context.getSymbolInfo(variableExpression.getVariableName());
    }

    @Override
    public SymbolInfo visit(RelationalExpression relationalExpression, Context context) {
        SymbolInfo leftExpVal = relationalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rightExpVal = relationalExpression.getRightExpression().accept(this, context);

        return typeCheckAndApplyRelationalExpression(leftExpVal, rightExpVal, relationalExpression.getOperator());
    }

    @Override
    public SymbolInfo visit(LogicalExpression logicalExpression, Context context) {
        SymbolInfo leftExpVal = logicalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rightExpVal = logicalExpression.getRightExpression().accept(this, context);

        Type lhsType = leftExpVal.getDataType();
        Type rhsType = rightExpVal.getDataType();

        //TODO rewrite the conditional operator
        if (lhsType == Type.BOOL && rhsType == Type.BOOL) {
            return new SymbolInfo(logicalExpression.getOperator() == Token.OR
                    ? leftExpVal.getBoolValue() || rightExpVal.getBoolValue() :
                    logicalExpression.getOperator() == Token.ANDAND
                            ? leftExpVal.getBoolValue() && rightExpVal.getBoolValue() : false);
        } else {
            throw new RuntimeException("Logical expressions are supported with boolean expressions only");
        }

    }

    @Override
    public SymbolInfo visit(NotExpression notExpression, Context context) {
        SymbolInfo expVal = notExpression.getExpression().accept(this, context);
        if(expVal.getDataType() != Type.BOOL) {
            throw new RuntimeException("Not Expression is supported with boolean values only");
        }

        return new SymbolInfo(!expVal.getBoolValue());
    }

    @Override
    public SymbolInfo visit(LambdaExpression lambdaExpression, Context context) {
        //TODO tree walk and find the correct variable to capture
        final Function function = lambdaExpression.getFunction().clone();
        function.setCapturedVariables(new LinkedHashMap<>(context.getSymbolTable()));
        return SymbolInfo.builder().withFunctionValue(function).withDataType(Type.FUNCTION).build();
    }

    private SymbolInfo typeCheckAndApplyRelationalExpression(SymbolInfo leftExpVal, SymbolInfo rightExpVal, Token operator) {
        Type lhsType = leftExpVal.getDataType();
        Type rhsType = rightExpVal.getDataType();
        switch (operator) {
            case DEQ:
                if(lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue().equals(rightExpVal.getIntegerValue()));
                } else if(lhsType == Type.INTEGER && rhsType == Type.LONG) {
                    return new SymbolInfo(Long.valueOf(leftExpVal.getIntegerValue()).equals(rightExpVal.getLongValue()));
                } else if(lhsType == Type.INTEGER && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getIntegerValue()).equals(rightExpVal.getFloatValue()));
                } else if(lhsType == Type.INTEGER && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getIntegerValue()).equals(rightExpVal.getDoubleValue()));
                } else if(lhsType == Type.LONG && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue().equals(Long.valueOf(rightExpVal.getIntegerValue())));
                } else if(lhsType == Type.LONG && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue().equals(rightExpVal.getLongValue()));
                } else if(lhsType == Type.LONG && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getLongValue()).equals(rightExpVal.getFloatValue()));
                } else if(lhsType == Type.LONG && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getLongValue()).equals(rightExpVal.getDoubleValue()));
                } else if(lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue().equals(Float.valueOf(rightExpVal.getIntegerValue())));
                } else if(lhsType == Type.FLOAT && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue().equals(Float.valueOf(rightExpVal.getLongValue())));
                } else if(lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue().equals(rightExpVal.getFloatValue()));
                } else if(lhsType == Type.FLOAT && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getFloatValue()).equals(rightExpVal.getDoubleValue()));
                } else if(lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue().equals(Double.valueOf(rightExpVal.getIntegerValue())));
                } else if(lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue().equals(Double.valueOf(rightExpVal.getLongValue())));
                } else if(lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue().equals(Double.valueOf(rightExpVal.getFloatValue())));
                } else if(lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue().equals(rightExpVal.getDoubleValue()));
                } else if(lhsType == Type.BOOL && rhsType == Type.BOOL) {
                    return new SymbolInfo(leftExpVal.getBoolValue().equals(rightExpVal.getBoolValue()));
                } else if(lhsType == Type.STRING && rhsType == Type.STRING) {
                    return new SymbolInfo(leftExpVal.getStringValue().equals(rightExpVal.getStringValue()));
                }

            case LT:
                if (lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() < rightExpVal.getIntegerValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.LONG) {
                    return new SymbolInfo(Long.valueOf(leftExpVal.getIntegerValue()) < rightExpVal.getLongValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getIntegerValue()) < rightExpVal.getFloatValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getIntegerValue()) < rightExpVal.getDoubleValue());
                } else if (lhsType == Type.LONG && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() < Long.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.LONG && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() < rightExpVal.getLongValue());
                } else if (lhsType == Type.LONG && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getLongValue()) < rightExpVal.getFloatValue());
                } else if (lhsType == Type.LONG && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getLongValue()) < rightExpVal.getDoubleValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() < Float.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() < Float.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() < rightExpVal.getFloatValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getFloatValue()) < rightExpVal.getDoubleValue());
                } else if (lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() < Double.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() < Double.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() < Double.valueOf(rightExpVal.getFloatValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() < rightExpVal.getDoubleValue());
                }
            case LTE:
                if (lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() <= rightExpVal.getIntegerValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.LONG) {
                    return new SymbolInfo(Long.valueOf(leftExpVal.getIntegerValue()) <= rightExpVal.getLongValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getIntegerValue()) <= rightExpVal.getFloatValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getIntegerValue()) <= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.LONG && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() <= Long.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.LONG && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() <= rightExpVal.getLongValue());
                } else if (lhsType == Type.LONG && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getLongValue()) <= rightExpVal.getFloatValue());
                } else if (lhsType == Type.LONG && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getLongValue()) <= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() <= Float.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() <= Float.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() <= rightExpVal.getFloatValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getFloatValue()) <= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() <= Double.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() <= Double.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() <= Double.valueOf(rightExpVal.getFloatValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() <= rightExpVal.getDoubleValue());
                }

            case GT:
                if (lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() > rightExpVal.getIntegerValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.LONG) {
                    return new SymbolInfo(Long.valueOf(leftExpVal.getIntegerValue()) > rightExpVal.getLongValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getIntegerValue()) > rightExpVal.getFloatValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getIntegerValue()) > rightExpVal.getDoubleValue());
                } else if (lhsType == Type.LONG && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() > Long.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.LONG && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() > rightExpVal.getLongValue());
                } else if (lhsType == Type.LONG && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getLongValue()) > rightExpVal.getFloatValue());
                } else if (lhsType == Type.LONG && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getLongValue()) > rightExpVal.getDoubleValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() > Float.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() > Float.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() > rightExpVal.getFloatValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getFloatValue()) > rightExpVal.getDoubleValue());
                } else if (lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() > Double.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() > Double.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() > Double.valueOf(rightExpVal.getFloatValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() > rightExpVal.getDoubleValue());
                }

            case GTE:
                if (lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() >= rightExpVal.getIntegerValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.LONG) {
                    return new SymbolInfo(Long.valueOf(leftExpVal.getIntegerValue()) >= rightExpVal.getLongValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getIntegerValue()) >= rightExpVal.getFloatValue());
                } else if (lhsType == Type.INTEGER && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getIntegerValue()) >= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.LONG && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() >= Long.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.LONG && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() >= rightExpVal.getLongValue());
                } else if (lhsType == Type.LONG && rhsType == Type.FLOAT) {
                    return new SymbolInfo(Float.valueOf(leftExpVal.getLongValue()) >= rightExpVal.getFloatValue());
                } else if (lhsType == Type.LONG && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getLongValue()) >= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() >= Float.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() >= Float.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() >= rightExpVal.getFloatValue());
                } else if (lhsType == Type.FLOAT && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(Double.valueOf(leftExpVal.getFloatValue()) >= rightExpVal.getDoubleValue());
                } else if (lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() >= Double.valueOf(rightExpVal.getIntegerValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() >= Double.valueOf(rightExpVal.getLongValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() >= Double.valueOf(rightExpVal.getFloatValue()));
                } else if (lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() >= rightExpVal.getDoubleValue());
                }

            default:
                    throw new RuntimeException("Unsupported operator : " + operator + ", on relational expression");
        }
    }

    public SymbolInfo visit(PrintStatement printStatement, Context context) {
        SymbolInfo exp = printStatement.getExpression().accept(this, context);
        if (Type.DOUBLE.equals(exp.getDataType())) {
            System.out.print(exp.getDoubleValue());
        } else if (Type.INTEGER.equals(exp.getDataType())) {
            System.out.print(exp.getIntegerValue());
        } else if (Type.LONG.equals(exp.getDataType())) {
            System.out.print(exp.getLongValue());
        } else if (Type.FLOAT.equals(exp.getDataType())) {
            System.out.print(exp.getFloatValue());
        } else if (Type.STRING.equals(exp.getDataType())) {
            System.out.print(exp.getStringValue());
        } else if (Type.BOOL.equals(exp.getDataType())) {
            System.out.print(exp.getBoolValue());
        } else {
            throw new RuntimeException("Unknown Data Type");
        }
        return null;
    }

    public SymbolInfo visit(PrintlnStatement printlnStatement, Context context) {
        SymbolInfo exp = printlnStatement.getExpression().accept(this, context);
        if (Type.DOUBLE.equals(exp.getDataType())) {
            System.out.println(exp.getDoubleValue());
        } else if (Type.INTEGER.equals(exp.getDataType())) {
            System.out.println(exp.getIntegerValue());
        } else if (Type.LONG.equals(exp.getDataType())) {
            System.out.println(exp.getLongValue());
        } else if (Type.FLOAT.equals(exp.getDataType())) {
            System.out.println(exp.getFloatValue());
        } else if (Type.STRING.equals(exp.getDataType())) {
            System.out.println(exp.getStringValue());
        } else if (Type.BOOL.equals(exp.getDataType())) {
            System.out.println(exp.getBoolValue());
        } else {
            throw new RuntimeException("Unknown Data Type");
        }
        return null;
    }

    public SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context) {
        VariableExpression variableExpression= variableDeclarationStatement.getVariableExpression();
        String variableToBeDeclared = variableExpression.getVariableName();
        SymbolInfo temp = context.getSymbolInfoFromCurrentScope(variableToBeDeclared);
        if(null != temp) {
            throw new RuntimeException("Variable '" + temp.getVariableName() + "' is already defined");
        }
        SymbolInfo decSymbolInfo = new SymbolInfo(null, variableToBeDeclared);
        context.addToSymbolTable(variableToBeDeclared, decSymbolInfo);
        return decSymbolInfo;
    }

    public SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context) {
        //LHS
        SymbolInfo lhsInfo = context.getSymbolInfo(variableAssignmentStatement.getVariableName());
        if(null == lhsInfo) {
            throw new RuntimeException("Undefined Variable : " + variableAssignmentStatement.getVariableName());
        }
        Type lhsType = lhsInfo.getDataType();

        //RHS
        SymbolInfo rhsInfo = variableAssignmentStatement.getExpression().accept(this, context);
        if(null == rhsInfo) {
            throw new RuntimeException("Undefined Variable : " +
                    VariableExpression.class.cast(variableAssignmentStatement.getExpression()).getVariableName());
        }
        Type rhsType = rhsInfo.getDataType();
        if(null == lhsType && null != rhsType) {
            //when lhs is declared and rhs has value
            if (Type.FLOAT.equals(rhsInfo.getDataType())) {
                lhsInfo.setFloatValue(rhsInfo.getFloatValue());
            } else if (Type.DOUBLE.equals(rhsInfo.getDataType())) {
                lhsInfo.setDoubleValue(rhsInfo.getDoubleValue());
            } else if (Type.INTEGER.equals(rhsInfo.getDataType())) {
                lhsInfo.setIntegerValue(rhsInfo.getIntegerValue());
            } else if (Type.LONG.equals(rhsInfo.getDataType())) {
                lhsInfo.setLongValue(rhsInfo.getLongValue());
            } else if (Type.STRING.equals(rhsInfo.getDataType())) {
                lhsInfo.setStringValue(rhsInfo.getStringValue());
            } else if (Type.BOOL.equals(rhsInfo.getDataType())) {
                lhsInfo.setBoolValue(rhsInfo.getBoolValue());
            } else if (Type.FUNCTION.equals(rhsInfo.getDataType())) {
                lhsInfo.setFunctionValue(rhsInfo.getFunctionValue());
            }
        } else if(null != lhsType && null == rhsType) {
            //assigning already declared but not assigned variable to lhs
            lhsInfo.nullify();
        } else if(null != lhsInfo && null != rhsInfo) {
            //mutating lhs with value of rhs
            if(lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                lhsInfo.setDoubleValue(rhsInfo.getDoubleValue());
            } else if(lhsType == Type.DOUBLE && rhsType == Type.FLOAT) {
                lhsInfo.setDoubleValue(Double.valueOf(rhsInfo.getFloatValue()));
            } else if(lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                lhsInfo.setDoubleValue(Double.valueOf(rhsInfo.getIntegerValue()));
            } else if(lhsType == Type.DOUBLE && rhsType == Type.LONG) {
                lhsInfo.setDoubleValue(Double.valueOf(rhsInfo.getLongValue()));
            } else if(lhsType == Type.FLOAT && rhsType == Type.FLOAT) {
                lhsInfo.setFloatValue(rhsInfo.getFloatValue());
            } else if(lhsType == Type.FLOAT && rhsType == Type.LONG) {
                lhsInfo.setFloatValue(Float.valueOf(rhsInfo.getLongValue()));
            } else if(lhsType == Type.FLOAT && rhsType == Type.INTEGER) {
                lhsInfo.setFloatValue(Float.valueOf(rhsInfo.getIntegerValue()));
            } else if(lhsType == Type.LONG && rhsType == Type.LONG) {
                lhsInfo.setLongValue(rhsInfo.getLongValue());
            } else if(lhsType == Type.LONG && rhsType == Type.INTEGER) {
                lhsInfo.setLongValue(Long.valueOf(rhsInfo.getIntegerValue()));
            } else if(lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                lhsInfo.setIntegerValue(rhsInfo.getIntegerValue());
            } else if(lhsType == Type.BOOL && rhsType == Type.BOOL) {
                lhsInfo.setBoolValue(rhsInfo.getBoolValue());
            } else if(lhsType == Type.STRING && rhsType == Type.STRING) {
                lhsInfo.setStringValue(rhsInfo.getStringValue());
            } else {
                throw new RuntimeException("Unsupported types lhs : " + lhsType + ", rhs : " + rhsType);
            }

        }
        //if lhs is just declared and rhs is also just declared then do nothing
        return lhsInfo;
    }

    @Override
    public SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context) {
        variableDeclAndAssignStatement.getVariableDeclarationStatement().accept(this, context);
        return variableDeclAndAssignStatement.getVariableAssignmentStatement().accept(this, context);
    }

    @Override
    public SymbolInfo visit(IfStatement ifStatement, Context context) {
        SymbolInfo symbolInfo = ifStatement.getBooleanExpression().accept(this, context);
        if(Type.BOOL != symbolInfo.getDataType()) {
            throw new RuntimeException("If condition expression should be of type boolean");
        }

        Context ifContext = new LexicalContext(context);

        if(symbolInfo.getBoolValue() == true) {
            for (Statement statement : ifStatement.getTrueBody()) {
                statement.accept(this, ifContext);
                //Todo evaluate the behaviour of break
                if (ifContext.getSymbolInfo("break") != null) {
                    context.addToSymbolTable("break", new SymbolInfo());
                    break;
                } else if (ifContext.getSymbolInfo("return") != null) {
                    context.addToSymbolTable("return", ifContext.getSymbolInfo("return"));
                    break;
                }
            }
        } else {
            for (Statement statement : ifStatement.getFalseBody()) {
                statement.accept(this, ifContext);
                //Todo evaluate the behaviour of break
                if (ifContext.getSymbolInfo("break") != null) {
                    context.addToSymbolTable("break", new SymbolInfo());
                    break;
                } else if (ifContext.getSymbolInfo("return") != null) {
                    context.addToSymbolTable("return", ifContext.getSymbolInfo("return"));
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public SymbolInfo visit(WhileStatement whileStatement, Context context) {
        SymbolInfo symbolInfo = whileStatement.getExpression().accept(this, context);

        if(Type.BOOL != symbolInfo.getDataType()) {
            throw new RuntimeException("While condition expression should be of type boolean");
        }

        slangWhile:
        while(symbolInfo.getBoolValue() == true) {
            Context whileContext = new LexicalContext(context);

            //Executing body of while
            for(Statement statement : whileStatement.getBody()) {
                statement.accept(this, whileContext);
                if(whileContext.getSymbolInfo("break") != null) {
                    break slangWhile;
                } else if (whileContext.getSymbolInfo("return") != null) {
                    context.addToSymbolTable("return", whileContext.getSymbolInfo("return"));
                    break;
                }
            }
            symbolInfo = whileStatement.getExpression().accept(this, context);
        }

        return null;
    }

    @Override
    public SymbolInfo visit(BreakStatement breakStatement, Context context) {
        context.addToSymbolTable("break", new SymbolInfo());
        return null;
    }

    @Override
    public SymbolInfo visit(Function function, Context context) {
//        context.addToFunctionTable(function.getName(), function);
        return null;
    }

    @Override
    public SymbolInfo visit(ReturnStatement returnStatement, Context context) {
        SymbolInfo returnSymbolInfo = returnStatement.getExpression().accept(this, context);
        context.addToSymbolTable("return", returnSymbolInfo);
        return null;
    }

    @Override
    public SymbolInfo visit(FunctionInvokeExpression functionInvokeExpression, Context context) {
        Function function = context.getFunction(functionInvokeExpression.getFunctionName());

        if(null == function) {
            SymbolInfo lambdaSymbol = context.getSymbolInfo(functionInvokeExpression.getFunctionName());
            if(null == lambdaSymbol) {
                throw new RuntimeException("Undefined function : " + functionInvokeExpression.getFunctionName());
            }

            if(Type.FUNCTION != lambdaSymbol.getDataType()) {
                throw new RuntimeException(functionInvokeExpression.getFunctionName() + " is not a function type");
            }

            function = lambdaSymbol.getFunctionValue();
        }

        if(null == function) {
            throw new RuntimeException("Undefined function : " + functionInvokeExpression.getFunctionName());
        }
        List<SymbolInfo> actualParams = functionInvokeExpression.getActualFunctionArguments()
                .stream()
                .map(expression -> expression.accept(this, context))
                .collect(Collectors.toList());

        if(actualParams.size() != function.getFormalArguments().entrySet().size()) {
            throw new RuntimeException("Formal and actual param size doesn't match");
        }

        Context functionContext = new LexicalContext(context.getFunctionTable());

        if(null != function.getCapturedVariables()) {
            for(Map.Entry<String, SymbolInfo> capturedEntry : function.getCapturedVariables().entrySet()) {
                functionContext.addToSymbolTable(capturedEntry.getKey(), capturedEntry.getValue());
            }
        }

        Set<Map.Entry<String, Type>> formalParams = function.getFormalArguments().entrySet();

        int i = 0;
        for (Map.Entry<String, Type> formalParam : function.getFormalArguments().entrySet()) {
            if (actualParams.get(i).getDataType() != formalParam.getValue()) {
                throw new RuntimeException("Actual and formal params data type is not matching");
            }

            functionContext.addToSymbolTable(formalParam.getKey(), actualParams.get(i));
            i++;
        }

        boolean foundReturn = false;
        for(Statement statement : function.getBody()) {
            statement.accept(this, functionContext);
            SymbolInfo returnInfo = functionContext.getSymbolInfo("return");
            if(null != returnInfo && (function.getReturnType() != returnInfo.getDataType())) {
                throw new RuntimeException("Return type doesn't match the function definition");
            }
            if(null != returnInfo) {
                foundReturn = true;
                return returnInfo;
            }
        }

        if(!foundReturn) {
            throw new RuntimeException("Expecting a return statement in AST");
        }

        return null;
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
        Context moduleContext = new LexicalContext(context, module.getFunctionsMap());
        new FunctionInvokeExpression("main", new ArrayList<>()).accept(this, moduleContext);
        return null;
    }

    //Type check helpers
    private SymbolInfo typeCheckAndApplyArithmeticOperator(SymbolInfo leftExpVal, SymbolInfo rightExpVal, Token operator) {
        switch (operator) {
            case ADD:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() + rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() + rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() + rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() + rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() + rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getFloatValue() + rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() + rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() + rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getLongValue() + rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getLongValue() + rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() + rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() + rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() + rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() + rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() + rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() + rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.STRING && rightExpVal.getDataType() == Type.STRING) {
                    return new SymbolInfo(leftExpVal.getStringValue() + rightExpVal.getStringValue());
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case SUB:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() - rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() - rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() - rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() - rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() - rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getFloatValue() - rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() - rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() - rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getLongValue() - rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getLongValue() - rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() - rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() - rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() - rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() - rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() - rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() - rightExpVal.getIntegerValue());
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case DIV:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() / rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() / rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() / rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() / rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() / rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getFloatValue() / rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() / rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() / rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getLongValue() / rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getLongValue() / rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getLongValue() / (double)rightExpVal.getLongValue())
                            .build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getLongValue() / (double)rightExpVal.getIntegerValue())
                            .build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() / rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() / rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getIntegerValue() / (double)rightExpVal.getLongValue())
                            .build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getIntegerValue() / (double)rightExpVal.getIntegerValue())
                            .build();
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case MUL:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() * rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() * rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() * rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getDoubleValue() * rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getFloatValue() * rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getFloatValue() * rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getFloatValue() * rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getFloatValue() * rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getLongValue() * rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getLongValue() * rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getLongValue() * rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getLongValue() * rightExpVal.getIntegerValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() * rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() * rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() * rightExpVal.getLongValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() * rightExpVal.getIntegerValue());
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            default:
                throw new RuntimeException("Unsupported Operator: " + operator);
        }
    }

}
