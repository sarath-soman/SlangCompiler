package com.slang.ast;

import com.slang.ValueInfo;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 19/3/17.
 */
public class StringLiteral extends Expression {

    private String stringLiteral;

    public StringLiteral(String stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    public ValueInfo accept(IVisitor visitor) {
        return visitor.visit(this);
    }

    public String getStringLiteral() {
        return stringLiteral;
    }
}
