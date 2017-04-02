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
        tokenMap.put("int", Token.INT);
        tokenMap.put("long", Token.LONG);
        tokenMap.put("float", Token.FLOAT);
        tokenMap.put("double", Token.DOUBLE);
        tokenMap.put("boolean", Token.BOOL);
        tokenMap.put("string", Token.STRING);
        tokenMap.put("var", Token.VAR);
        tokenMap.put("true", Token.TRUE);
        tokenMap.put("false", Token.FALSE);
        tokenMap.put("if", Token.IF);
        tokenMap.put("endif", Token.ENDIF);
        tokenMap.put("then", Token.THEN);
        tokenMap.put("else", Token.ELSE);
        tokenMap.put("while", Token.WHILE);
        tokenMap.put("wend", Token.WEND);
        tokenMap.put("break", Token.BREAK);
        tokenMap.put("function", Token.FUNCTION);
        tokenMap.put("return", Token.RETURN);
        tokenMap.put("void", Token.VOID);
        tokenMap.put("end", Token.END);
    }

    public static Token getToken(String keyword) {
        Token token = tokenMap.get(keyword);
        return  null == token ? Token.UNKNOWN : token;
    }
}
