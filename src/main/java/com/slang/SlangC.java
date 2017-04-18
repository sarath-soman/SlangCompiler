package com.slang;


import com.slang.ast.Module;
import com.slang.lexer.Lexer;
import com.slang.parser.Parser;
import com.slang.visitor.Interpreter;
import com.slang.visitor.LexicalContext;
import com.slang.visitor.SemanticAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by sarath on 16/3/17.
 */
public class SlangC {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("file name required");
            System.exit(0);
        }

        String moduleString = new String(Files.readAllBytes(new File(args[0]).toPath()));


        Lexer lexer = new Lexer(moduleString);
        Parser parser = new Parser(lexer);
        Module module = parser.parseModule();
        System.out.println(module);
//        module.accept(new SemanticAnalyzer(), new LexicalContext());
        module.accept(new Interpreter(), new LexicalContext());
    }
}
