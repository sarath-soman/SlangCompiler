package com.slang.visitor;

import com.slang.SymbolInfo;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitable {

    SymbolInfo accept(IVisitor visitor, Context context);

}
