package com.slang.lexer;

import com.slang.ast.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarath on 18/3/17.
 */
public class TokenLookup {

    private static Map<String, Token> tokenMap = new HashMap<String, Token>();

    static  {
        tokenMap.put("print", Token.PRINT);
        tokenMap.put("println", Token.PRINTLN);
    }

    public static Token getToken(String keyword) {
        Token token = tokenMap.get(keyword);
        return  null == token ? Token.UNKNOWN : token;
    }
}
