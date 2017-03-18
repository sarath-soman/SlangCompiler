package com.slang.lexer;

import com.slang.ast.Token;

/**
 * Created by sarath on 16/3/17.
 */
public class Lexer {

    private Token previousToken;
    private Token currentToken;
    private final String module;
    private final int moduleLen;
    private int index;
    private double num;

    public Lexer(String module) {
        this.module = module;
        moduleLen = module.length();
    }

    public void eat() {

        if(isEndOfModule()) {
            previousToken = currentToken;
            currentToken = Token.UNKNOWN;
        }

        while(isNotEndOfModule()) {
            switch (module.charAt(index)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    num = readNum();
                    previousToken = currentToken;
                    currentToken = Token.NUM;
            }
        }

    }

    private double readNum() {
        return 0;
    }

    public boolean isEndOfModule() {
        if(index < moduleLen) {
            return false;
        }

        return true;
    }

    public boolean isNotEndOfModule() {
        return !isEndOfModule();
    }

    public Token getPreviousToken() {
        return previousToken;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getModule() {
        return module;
    }


    public int getModuleLen() {
        return moduleLen;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getNum() {
        return num;
    }
}
