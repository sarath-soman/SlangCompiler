package com.slang.visitor;

import com.slang.Value;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitable {

    Value accept(IVisitor visitor);

}
