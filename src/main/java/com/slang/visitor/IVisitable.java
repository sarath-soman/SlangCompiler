package com.slang.visitor;

import com.slang.ast.Expression;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitable {

    Expression accept(IVisitor visitor);

}
