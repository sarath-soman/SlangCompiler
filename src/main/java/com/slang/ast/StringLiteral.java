package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 19/3/17.
 */
public class StringLiteral extends Expression {

    private String stringLiteral;

    public StringLiteral(String stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public String getStringLiteral() {
        return stringLiteral;
    }
}
