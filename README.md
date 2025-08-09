## SlangCompiler

A small, expression-first toy language with functions, variables, conditionals, loops, and lambdas. This repo contains a hand-written lexer and recursive-descent parser with a semantic analyzer and interpreter, plus an ANTLR4 grammar and an EBNF spec for reference.

### Highlights
- Primitive types: `int`, `long`, `float`, `double`, `boolean`, `string`, `void`
- User functions with typed params and return types
- Variables: declaration `var x;` and assignment `x = expr;`
- Control flow: `if/then/else/endif`, `while ... wend`, `break`
- Printing: `print expr;`, `println expr;`
- Lambdas with explicit types: `lambda int (int a) ... endlambda`
- Function types: `(T1, T2, ...) -> R`

### Grammar
- EBNF: `grammar.ebnf`
- ANTLR4: `src/main/antlr4/Slang.g4`

Operator precedence (high to low): unary `! + -`, `* /`, `+ -`, relational `== < <= > >=`, logical `&& ||`.

Semicolons are required after statements and function calls. Lambdas end with `endlambda` (no trailing semicolon).

### Quick start (hand-written compiler/interpreter)
Requires Java 8+ and Maven.

Build and run tests:

```bash
mvn -q -e -DskipTests=false test
```

Run the interpreter on an example program:

```bash
mvn -q -DskipTests package
java -cp target/slang-compiler-0.1.jar com.slang.SlangC src/main/java/hello.sl
```

### Language examples

Minimal function module:

```text
function int add(int a, int b)
  return a + b;
end

function void main()
  var x = add(2, 3);
  println x;
end
```

If/while and lambdas:

```text
function void main()
  var inc = lambda int (int n)
    return n + 1;
  endlambda

  var i = 0;
  while i < 3
    println i;
    i = inc(i);
  wend

  if i == 3 then
    println "done";
  else
    println "unexpected";
  endif
end
```

### ANTLR4 usage (optional)
An ANTLR4 grammar is included at `src/main/antlr4/Slang.g4`. You can generate a parser with the ANTLR toolchain.

Using the ANTLR command-line tool:

```bash
# Install ANTLR4 if needed, e.g., via Homebrew
brew install antlr

# Generate Java parser/lexer into ./generated
antlr4 -Dlanguage=Java -visitor -listener -o generated src/main/antlr4/Slang.g4

# Compile the generated sources (example)
javac -cp $(antlr4 -cp) generated/*.java
```

Using Maven (add to your own pom):

```xml
<plugin>
  <groupId>org.antlr</groupId>
  <artifactId>antlr4-maven-plugin</artifactId>
  <version>4.13.1</version>
  <executions>
    <execution>
      <goals>
        <goal>antlr4</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <listener>true</listener>
    <visitor>true</visitor>
    <packageName>com.slang.antlr</packageName>
  </configuration>
</plugin>
```

This repository’s runtime uses the hand-written lexer/parser located in `src/main/java/com/slang/lexer` and `src/main/java/com/slang/parser`.

### Project layout
- `src/main/java/com/slang/` — Lexer, Parser, AST, visitors (semantic analysis, interpreter)
- `src/test/java/com/slang/` — Unit tests
- `src/main/antlr4/Slang.g4` — ANTLR4 grammar (reference)
- `grammar.ebnf` — EBNF grammar (reference)

### Running your own program
Create a `.sl` file and pass its absolute or relative path to `com.slang.SlangC`:

```bash
java -cp target/slang-compiler-0.1.jar com.slang.SlangC /absolute/path/to/your.sl
```

### Notes
- Identifiers are alphabetic followed by alphanumerics; keywords are reserved; `_` is not part of identifiers.
- Numeric literals: `123`, `123l`, `1.23`, `1.23f`.
- Strings use double quotes, without escape sequences.

### Attribution
This project acknowledges and links to the original Slang work by **Praseed Pai** (the father of the original Slang compiler):

- GitHub profile: [praseedpai](https://github.com/praseedpai)
- Slang Compiler Infrastructure (learning project): [SlangForDotNet](https://github.com/praseedpai/SlangForDotNet)


