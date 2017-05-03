package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by Sarath on 29/04/2017.
 */
public class ObjectMemberVariableExpression extends Expression{

    private String objectName;
    private String memberName;


    public ObjectMemberVariableExpression(String objectName, String memberName) {
        this.objectName = objectName;
        this.memberName = memberName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getMemberName() {
        return memberName;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "ObjectMemberVariableExpression{" +
                "objectName='" + objectName + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
