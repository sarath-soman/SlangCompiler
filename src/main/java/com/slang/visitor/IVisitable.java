package com.slang.visitor;

import com.slang.ValueInfo;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitable {

    ValueInfo accept(IVisitor visitor);

}
