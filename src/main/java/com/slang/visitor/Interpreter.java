package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpreter implements IVisitor {

    public SymbolInfo visit(NumericExpression expression, Context context) {
        switch (expression.getDataType()) {
            case DOUBLE:
                return new SymbolInfo(expression.getDoubleValue());
            case FLOAT:
                return new SymbolInfo(expression.getFloatValue());
            case LONG:
                return new SymbolInfo(expression.getLongValue());
            case INTEGER:
                return new SymbolInfo(expression.getIntegerValue());

            default:
                throw new RuntimeException("Unsupported data type");
        }
    }

    public SymbolInfo visit(UnaryExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        if(Token.SUB != expression.getOperator()) {
            switch (leftExpVal.getDataType()) {
                case DOUBLE:
                case FLOAT:
                case LONG:
                case INTEGER:
                    break;
                default:
                    throw new RuntimeException("Unsupported data type : " + leftExpVal.getDataType());
            }
            return leftExpVal;
        }

        //When oper is SUB
        switch (leftExpVal.getDataType()) {
            case DOUBLE:
                return new SymbolInfo(leftExpVal.getDoubleValue() * -1);
            case FLOAT:
                return new SymbolInfo(leftExpVal.getFloatValue() * -1);
            case LONG:
                return new SymbolInfo(leftExpVal.getLongValue() * -1);
            case INTEGER:
                return new SymbolInfo(leftExpVal.getIntegerValue() * -1);

            default:
                throw new RuntimeException("Unsupported data type : " + leftExpVal.getDataType());
        }
    }

    public SymbolInfo visit(BinaryExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        SymbolInfo rightExpVal = expression.getRightExpression().accept(this, context);
        Token token = expression.getOperator();
        SymbolInfo resp = typeCheckAndApplyBinaryOperator(leftExpVal, rightExpVal, token);
        System.out.println("TY " + resp);
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

    public SymbolInfo visit(PrintStatement printStatement, Context context) {
        SymbolInfo exp = printStatement.getExpression().accept(this, context);
        switch (exp.getDataType()) {
            case DOUBLE:
                System.out.print(exp.getDoubleValue());
                break;
            case INTEGER:
                System.out.print(exp.getIntegerValue());
                break;
            case LONG:
                System.out.print(exp.getLongValue());
                break;
            case FLOAT:
                System.out.print(exp.getFloatValue());
                break;
            case STRING:
                System.out.print(exp.getStringValue());
                break;
            case BOOL:
                System.out.print(exp.getBoolValue());
                break;

            default:
                throw new RuntimeException("Unknown Data Type");
        }
        return null;
    }

    public SymbolInfo visit(PrintlnStatement printlnStatement, Context context) {
        SymbolInfo exp = printlnStatement.getExpression().accept(this, context);
        switch (exp.getDataType()) {
            case DOUBLE:
                System.out.println(exp.getDoubleValue());
                break;
            case INTEGER:
                System.out.println(exp.getIntegerValue());
                break;
            case LONG:
                System.out.println(exp.getLongValue());
                break;
            case FLOAT:
                System.out.println(exp.getFloatValue());
                break;
            case STRING:
                System.out.println(exp.getStringValue());
                break;
            case BOOL:
                System.out.println(exp.getBoolValue());
                break;

            default:
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
            switch (rhsInfo.getDataType()) {
                case FLOAT:
                    lhsInfo.setFloatValue(rhsInfo.getFloatValue());
                    break;
                case DOUBLE:
                    lhsInfo.setDoubleValue(rhsInfo.getDoubleValue());
                    break;
                case INTEGER:
                    lhsInfo.setIntegerValue(rhsInfo.getIntegerValue());
                    break;
                case LONG:
                    lhsInfo.setLongValue(rhsInfo.getLongValue());
                    break;
                case STRING:
                    lhsInfo.setStringValue(rhsInfo.getStringValue());
                    break;
                case BOOL:
                    lhsInfo.setBoolValue(rhsInfo.getBoolValue());
                    break;
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

    //Type check helpers
    private SymbolInfo typeCheckAndApplyBinaryOperator(SymbolInfo leftExpVal, SymbolInfo rightExpVal, Token operator) {
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
                    return SymbolInfo.Builder.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getLongValue() / (double)rightExpVal.getLongValue())
                            .build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.Builder.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(leftExpVal.getLongValue() / (double)rightExpVal.getIntegerValue())
                            .build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() / rightExpVal.getFloatValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return new SymbolInfo(leftExpVal.getIntegerValue() / rightExpVal.getDoubleValue());
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.Builder.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(Double.valueOf(leftExpVal.getIntegerValue() / rightExpVal.getLongValue()))
                            .build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.Builder.builder()
                            .withDataType(Type.DOUBLE)
                            .withDoubleValue(Double.valueOf(leftExpVal.getIntegerValue() / rightExpVal.getIntegerValue()))
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
