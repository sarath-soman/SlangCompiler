package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.Token;

/**
 * Created by Sarath on 06/04/2017.
 */
public class TypeCheckerHelper {

    public static SymbolInfo checkArithmeticExpresion(SymbolInfo leftExpVal, SymbolInfo rightExpVal, Token operator) {
        switch (operator) {
            case ADD:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.INTEGER).build();
                } else if(leftExpVal.getDataType() == Type.STRING && rightExpVal.getDataType() == Type.STRING) {
                    return SymbolInfo.builder().withDataType(Type.STRING).build();
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case SUB:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.INTEGER).build();
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case DIV:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            case MUL:
                if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.DOUBLE && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.FLOAT && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.LONG && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.FLOAT) {
                    return SymbolInfo.builder().withDataType(Type.FLOAT).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.DOUBLE) {
                    return SymbolInfo.builder().withDataType(Type.DOUBLE).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.LONG) {
                    return SymbolInfo.builder().withDataType(Type.LONG).build();
                } else if(leftExpVal.getDataType() == Type.INTEGER && rightExpVal.getDataType() == Type.INTEGER) {
                    return SymbolInfo.builder().withDataType(Type.INTEGER).build();
                } else {
                    throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType());
                }
            default:
                throw new RuntimeException("Unsupported Operator: " + operator);
        }
    }

    public static SymbolInfo checkRelationalExpression(SymbolInfo leftExpVal, SymbolInfo rightExpVal, Token operator) {
        Type lhsType = leftExpVal.getDataType();
        Type rhsType = rightExpVal.getDataType();
        boolean isValidExpression = false;
        switch (operator) {
            case DEQ:
            case LT:
            case LTE:
            case GT:
            case GTE:
                if((lhsType == Type.INTEGER && rhsType == Type.INTEGER) || (lhsType == Type.INTEGER && rhsType == Type.LONG)
                        || (lhsType == Type.INTEGER && rhsType == Type.FLOAT) || (lhsType == Type.INTEGER && rhsType == Type.DOUBLE)
                        || (lhsType == Type.LONG && rhsType == Type.INTEGER) || (lhsType == Type.LONG && rhsType == Type.LONG)
                        || (lhsType == Type.LONG && rhsType == Type.FLOAT) || (lhsType == Type.LONG && rhsType == Type.DOUBLE)
                        || (lhsType == Type.FLOAT && rhsType == Type.INTEGER) || (lhsType == Type.FLOAT && rhsType == Type.LONG)
                        || (lhsType == Type.FLOAT && rhsType == Type.FLOAT) || (lhsType == Type.FLOAT && rhsType == Type.DOUBLE)
                        || (lhsType == Type.DOUBLE && rhsType == Type.INTEGER) || (lhsType == Type.DOUBLE && rhsType == Type.LONG)
                        || (lhsType == Type.DOUBLE && rhsType == Type.FLOAT) || (lhsType == Type.DOUBLE && rhsType == Type.DOUBLE)
                        || (lhsType == Type.BOOL && rhsType == Type.BOOL)) {
                    isValidExpression = true;
                    break;
                }
        }

        if(!isValidExpression) {
            if(operator == Token.DEQ && (lhsType == Type.STRING && rhsType == Type.STRING)) {
                isValidExpression = true;
            }
        }

        if (isValidExpression) {
            return SymbolInfo.builder().withDataType(Type.BOOL).build();
        }

        throw new RuntimeException("Unsupported types lhs : " + leftExpVal.getDataType() + ", rhs : " + rightExpVal.getDataType() + " on operator " + operator);

    }

    public static boolean isEqual(Type type1, Type type2) {
        return type1.getTypeName().equals(type2.getTypeName());
    }

    public static boolean isNotEqual(Type type1, Type type2) {
        return !isEqual(type1, type2);
    }
}
